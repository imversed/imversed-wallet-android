package com.fulldive.wallet.presentation.security.password

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.secret.SecretInteractor
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.fulldive.wallet.presentation.system.keyboard.KeyboardType
import com.joom.lightsaber.ProvidedBy
import moxy.MvpAppCompatActivity
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.utils.WLog
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class CheckPasswordPresenter @Inject constructor(
    private val secretInteractor: SecretInteractor
) : BaseMoxyPresenter<CheckPasswordMoxyView>() {
    private var userInput: String = ""
    private var askQuite = false

    override fun attachView(view: CheckPasswordMoxyView) {
        super.attachView(view)
        clear()
    }

    fun onUserInsertKey(input: Char) {
        var updateField = false

        if (userInput.isEmpty()) {
            userInput = input.toString()
            updateField = true
        } else if (userInput.length < 5) {
            userInput += input
            updateField = true
        }
        when {
            userInput.length == 4 -> {
                viewState.switchKeyboard(KeyboardType.Alphabet)
            }
            userInput.length == 5 && secretInteractor.isPasswordValid(userInput) -> {
                checkPassword()
            }
            userInput.length == 5 -> {
                clear()
                updateField = false
            }
        }
        if (updateField) {
            askQuite = false
            viewState.updatePasswordField(userInput.length)
        }
    }

    fun onBackPressed() {
        when {
            userInput.isNotEmpty() -> userDeleteKey()
            askQuite -> viewState.finishWithResult(MvpAppCompatActivity.RESULT_CANCELED)
            else -> {
                askQuite = true
                viewState.showMessage(R.string.str_ready_to_quite)
            }
        }
    }

    fun userDeleteKey() {
        when {
            userInput.isEmpty() -> onBackPressed()
            userInput.length == 4 -> {
                userInput = userInput.substring(0, userInput.length - 1)
                viewState.switchKeyboard(KeyboardType.Numeric)
            }
            else -> {
                userInput = userInput.substring(0, userInput.length - 1)
            }
        }
        viewState.updatePasswordField(userInput.length)
    }

    fun onShakeEnded() {
        clear()
    }

    private fun clear() {
        userInput = ""
        viewState.clear()
        viewState.switchKeyboard(KeyboardType.Numeric)
    }

    private fun checkPassword() {
        viewState.showWaitDialog()
        secretInteractor
            .checkPassword(userInput)
            .withDefaults()
            .compositeSubscribe(
                onSuccess = { isCorrect ->
                    WLog.w("Password was checked")
                    if (isCorrect) {
                        viewState.finishWithResult(MvpAppCompatActivity.RESULT_OK)
                    } else {
                        viewState.hideWaitDialog()
                        viewState.shakeView()
                        clear()
                        viewState.showMessage(R.string.error_invalid_password)
                    }
                }
            ) { error: Throwable ->
                viewState.showMessage(R.string.str_unknown_error_msg)
                viewState.shakeView()
                viewState.hideWaitDialog()
                clear()
            }
    }
}