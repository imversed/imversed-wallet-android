package com.fulldive.wallet.presentation.security.mnemonic.layout

import android.content.Context
import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.joom.lightsaber.ProvidedBy
import wannabit.io.cosmostaion.base.BaseChain
import wannabit.io.cosmostaion.utils.WLog
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class MnemonicPresenter @Inject constructor(
    private val context: Context
) : BaseMoxyPresenter<MnemonicMoxyView>() {

    private var chain: BaseChain? = null
    private var mnemonicWords: List<String> = emptyList()

    override fun attachView(view: MnemonicMoxyView) {
        super.attachView(view)
        WLog.w("fftf, MnemonicPresenter.attachView")
    }

    override fun detachView(view: MnemonicMoxyView) {
        WLog.w("fftf, MnemonicPresenter.detachView")
        super.detachView(view)
    }

    fun onChainChanged(chain: BaseChain) {
        this.chain = chain
        viewState.setColors(chain.chainBackground, chain.mnemonicBackground)
    }

    fun onMnemonicChanged(mnemonicWords: List<String>) {
        this.mnemonicWords = mnemonicWords
        viewState.showMnemonicWords(mnemonicWords)
    }
}