package com.fulldive.wallet.presentation.security.mnemonic.layout

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.joom.lightsaber.ProvidedBy
import wannabit.io.cosmostaion.base.BaseChain
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class MnemonicPresenter @Inject constructor() : BaseMoxyPresenter<MnemonicMoxyView>() {

    private var chain: BaseChain? = null
    private var mnemonicWords: List<String> = emptyList()

    fun onChainChanged(chain: BaseChain) {
        this.chain = chain
        viewState.setColors(chain.chainBackground, chain.mnemonicBackground)
    }

    fun onMnemonicChanged(mnemonicWords: List<String>) {
        this.mnemonicWords = mnemonicWords
        viewState.showMnemonicWords(mnemonicWords)
    }
}