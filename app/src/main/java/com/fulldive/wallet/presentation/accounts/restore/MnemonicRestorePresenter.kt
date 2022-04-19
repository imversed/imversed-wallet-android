package com.fulldive.wallet.presentation.accounts.restore

import android.os.Bundle
import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.*
import com.fulldive.wallet.interactors.ClipboardInteractor
import com.fulldive.wallet.interactors.ScreensInteractor
import com.fulldive.wallet.interactors.secret.MnemonicUtils
import com.fulldive.wallet.interactors.secret.SecretInteractor
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.fulldive.wallet.presentation.chains.choicenet.ChoiceChainDialogFragment
import com.fulldive.wallet.rx.AppSchedulers
import com.joom.lightsaber.ProvidedBy
import wannabit.io.cosmostaion.R
import com.fulldive.wallet.models.BaseChain
import wannabit.io.cosmostaion.dialog.*
import java.util.*
import javax.inject.Inject
import kotlin.math.max

@ProvidedBy(DefaultPresentersModule::class)
class MnemonicRestorePresenter @Inject constructor(
    private val secretInteractor: SecretInteractor,
    private val screensInteractor: ScreensInteractor,
    private val clipboardInteractor: ClipboardInteractor
) : BaseMoxyPresenter<MnemonicRestoreMoxyView>() {

    var chain: BaseChain? = null

    private val fields = Array(WORDS_COUNT) { "" }
    private var currentField = 0
    private var customPath = 0
    private var extras: Bundle? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        secretInteractor
            .getMnemonicDictionary()
            .withUiDefaults()
            .compositeSubscribe(
                onSuccess = viewState::setDictionary
            )

        if (chain == null) {
            requestChain()
        }
    }

    fun onSuggestionClicked(word: String) {
        val text = word.trim()
        fields[currentField] = text
        updateField(currentField, text, false)
        nextField()
    }

    fun onKeyboardKeyClicked(key: Char) {
        var text = fields[currentField]
        if (text.length + 1 < MAX_WORD_LENGTH) {
            text += key
            fields[currentField] = text
            updateField(currentField, text, false)
        }
    }

    fun onNextKeyClicked() {
        nextField()
    }

    fun onDeleteKeyClicked() {
        var text = fields[currentField]
        if (text.isNotEmpty()) {
            text = text.substring(0, text.length - 1)
            fields[currentField] = text
            updateField(currentField, text, false)
        } else if (currentField > 0) {
            --currentField
            updateField(currentField, fields[currentField], true)
        }
    }

    fun onFieldClicked(position: Int) {
        currentField = position
        updateField(currentField, fields[currentField], true)
    }

    fun onClearAllClicked() {
        currentField = 0
        fields.fill("")
        viewState.updateFields(fields, currentField)
        updateWordsCount()
    }

    fun onPasteClicked() {
        clipboardInteractor
            .getSafeClip()
            .onErrorResumeNext(clipboardInteractor.getClip())
            .map { text ->
                text
                    .trim()
                    .split(" ")
                    .map { it.filter(Char::isLetter) }
                    .filter(String::isNotEmpty)
            }
            .withUiDefaults()
            .compositeSubscribe(
                onSuccess = { words ->
                    val wordsArray = words.toTypedArray()
                    if (secretInteractor.isValidMnemonicWords(wordsArray)) {
                        fields.fill("")
                        wordsArray.copyInto(fields)
                        currentField = max(0, wordsArray.size - 1)
                        viewState.updateFields(fields, currentField)
                        updateWordsCount()
                    } else {
                        viewState.showMessage(R.string.error_clipboard_no_data)
                    }
                }
            )

    }

    fun onConfirmClicked() {
        extras = null
        safeSingle {
            var result: Bundle? = null
            if (secretInteractor.isValidMnemonicArray(fields)
                && MnemonicUtils.isValidStringHdSeedFromWords(fields.toList())
            ) {
                result = Bundle().apply {
                    val wordsList = fields.toList()
                    putString(
                        RestorePathActivity.KEY_ENTROPY,
                        secretInteractor.entropyHexFromMnemonicWords(wordsList)
                    )
                    putInt(RestorePathActivity.KEY_SIZE, wordsList.size)
                    putInt(RestorePathActivity.KEY_CUSTOM_PATH, customPath)
                    putString(RestorePathActivity.KEY_CHAIN, chain!!.chainName)
                }
            }
            result
        }
            .withDefaults()
            .compositeSubscribe(
                onSuccess = { extras ->
                    this.extras = extras
                    val dialog = when (chain) {
                        BaseChain.KAVA_MAIN -> Dialog_KavaRestorePath.newInstance()
                        BaseChain.SECRET_MAIN -> Dialog_SecretRestorePath.newInstance()
                        BaseChain.OKEX_MAIN -> Dialog_OkexRestoreType.newInstance()
                        BaseChain.FETCHAI_MAIN -> Dialog_FetchRestorePath.newInstance()
                        BaseChain.LUM_MAIN -> Dialog_LumRestorePath.newInstance()
                        else -> null
                    }
                    if (dialog != null) {
                        viewState.showDialog(dialog, "dialog", false)
                    } else {
                        customPath = 0
                        checkPassword()
                    }
                },
                onError = {
                    viewState.showMessage(R.string.error_invalid_mnemonic_count)
                }
            )
    }

    fun onCustomPathChanged(customPath: Int) {
        this.customPath = customPath
        checkPassword()
    }

    fun onPasswordStepPassed() {
        extras?.let(viewState::showRestorePathActivity)
    }

    private fun requestChain() {
        singleCallable { UUID.randomUUID().toString() }
            .subscribeOn(AppSchedulers.ui())
            .observeOn(AppSchedulers.ui())
            .flatMap { requestCode ->
                completeCallable {
                    val dialog = ChoiceChainDialogFragment.newInstance(
                        false,
                        requestCode,
                        isCheckLimit = true
                    )
                    viewState.showDialog(dialog, "dialog", false)
                }
                    .toSingleDefault(requestCode)
            }
            .flatMapObservable(screensInteractor::observeResult)
            .compositeSubscribe(
                onNext = { result ->
                    onChainReceived(result as? BaseChain)
                },
                onError = object : OnErrorConsumer() {
                    override fun onError(error: Throwable) {
                        super.onError(error)
                        onChainReceived(null)
                    }
                }
            )
    }

    private fun onChainReceived(baseChain: BaseChain?) {
        if (baseChain == null) {
            viewState.finish()
        } else {
            this.chain = baseChain
            viewState.showChain(baseChain)
            updateWordsCount()
        }
    }

    private fun nextField() {
        if (currentField < WORDS_COUNT - 1) {
            ++currentField
            updateField(currentField, fields[currentField], true)
        }
    }

    private fun updateWordsCount() {
        val count = fields.count(secretInteractor::isValidMnemonicWord)
        val isValidMnemonic = secretInteractor.isValidMnemonicArray(fields)
        val colorResId = if (isValidMnemonic || count == 0) {
            chain.or(BaseChain.COSMOS_MAIN).chainColor
        } else {
            R.color.colorRed
        }
        viewState.showWordsCount(count, colorResId)
    }

    private fun updateField(index: Int, text: String, requestFocus: Boolean) {
        viewState.updateField(index, text, requestFocus)
        updateWordsCount()
    }

    private fun checkPassword() {
        secretInteractor
            .isPasswordExists()
            .withDefaults()
            .compositeSubscribe(
                onSuccess = { hasPassword ->
                    if (hasPassword) {
                        viewState.checkPassword()
                    } else {
                        viewState.requestSetPassword()
                    }
                }
            )
    }

    companion object {
        private const val WORDS_COUNT = 24
        private const val MAX_WORD_LENGTH = 15
    }
}
