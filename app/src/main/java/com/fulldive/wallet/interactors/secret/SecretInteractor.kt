package com.fulldive.wallet.interactors.secret

import com.fulldive.wallet.di.modules.DefaultInteractorsModule
import com.fulldive.wallet.extensions.orNull
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.extensions.toSingle
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.local.AccountSecrets
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.crypto.MnemonicCode
import wannabit.io.cosmostaion.crypto.CryptoHelper
import wannabit.io.cosmostaion.crypto.EncResult
import java.security.SecureRandom
import java.util.regex.Pattern
import javax.inject.Inject

@ProvidedBy(DefaultInteractorsModule::class)
class SecretInteractor @Inject constructor(
    private val secretRepository: SecretRepository
) {

    fun createSecrets(chain: BaseChain, path: Int = 0): Single<AccountSecrets> {
        return safeSingle {
            val entropy = getEntropy()
            val words = MnemonicCode.INSTANCE.toMnemonic(entropy)
            val customPath = when (chain) {
                BaseChain.KAVA_MAIN,
                BaseChain.SECRET_MAIN,
                BaseChain.LUM_MAIN -> 1
                BaseChain.OKEX_MAIN -> 2
                else -> 0
            }
            val hexEntropy = MnemonicUtils.byteArrayToHexString(entropy)
            val address = MnemonicUtils.createAddress(
                chain,
                hexEntropy,
                path,
                customPath
            )

            AccountSecrets(hexEntropy, words, address, path, customPath)
        }
    }

    fun getRandomMnemonic(entropy: String): Single<List<String>> {
        return safeSingle {
            MnemonicCode.INSTANCE.toMnemonic(
                MnemonicUtils.hexStringToByteArray(entropy)
            )
        }
    }

    fun createKeyWithPathFromEntropy(
        chain: BaseChain,
        entropy: String,
        path: Int,
        customPath: Int
    ): Single<DeterministicKey> {
        return safeSingle {
            MnemonicUtils.createKeyWithPathFromEntropy(
                chain, entropy, path, customPath
            )
        }
    }

    fun deleteMnemonicKey(uuid: String): Completable {
        return safeCompletable {
            CryptoHelper.deleteKey(MNEMONIC_KEY + uuid)
        }
    }

    fun deletePrivateKey(uuid: String): Completable {
        return safeCompletable {
            CryptoHelper.deleteKey(PRIVATE_KEY + uuid)
        }
    }

    fun entropyToMnemonic(uuid: String, resource: String, spec: String): Single<String> {
        return safeSingle {
            CryptoHelper.decryptData(
                MNEMONIC_KEY + uuid,
                resource,
                spec
            )
        }
    }

    fun entropyFromMnemonic(uuid: String, entropy: String): Single<EncResult> {
        return encryptText(MNEMONIC_KEY + uuid, entropy)
    }

    fun entropyToPrivateKey(uuid: String, resource: String, spec: String): Single<String> {
        return safeSingle {
            CryptoHelper.decryptData(
                PRIVATE_KEY + uuid,
                resource,
                spec
            )
        }
    }

    fun entropyFromPrivateKey(uuid: String, entropy: String): Single<EncResult> {
        return encryptText(PRIVATE_KEY + uuid, entropy)
    }

    fun encryptText(key: String, text: String): Single<EncResult> {
        return safeSingle {
            CryptoHelper.encryptData(key, text, false)
        }
    }

    fun isPasswordValid(text: String): Boolean {
        var result = false
        if (text.length == 5) {
            result = Pattern
                .compile("^\\d{4}+[A-Z]$")
                .matcher(text)
                .matches()
        }
        return result
    }

    fun entropyHexFromMnemonicWords(words: List<String>): String {
        return MnemonicUtils.byteArrayToHexString(MnemonicCode.INSTANCE.toEntropy(words))
    }

    fun entropyFromMnemonicWords(words: List<String>): ByteArray {
        return MnemonicCode.INSTANCE.toEntropy(words)
    }

    fun isValidMnemonicArray(words: Array<String>): Boolean {
        return MNEMONIC_SIZES.contains(words.size) && isValidMnemonicWords(words)
    }

    fun isValidMnemonicWords(words: Array<String>): Boolean {
        val mnemonics = MnemonicCode.INSTANCE.wordList
        return words.all(mnemonics::contains)
    }

    fun checkMnemonicWords(words: Array<String>): List<Boolean> {
        val mnemonics = MnemonicCode.INSTANCE.wordList
        return words.map { word -> !mnemonics.contains(word) }
    }

    fun isValidMnemonicWord(word: String): Boolean {
        return word.isNotEmpty() && MnemonicCode.INSTANCE.wordList.contains(word)
    }

    fun getMnemonicDictionary(): Single<List<String>> {
        return MnemonicCode.INSTANCE.wordList.toSingle()
    }

    fun encrypt(salt: String, encDataString: String, ivDataString: String): Single<String> {
        return safeSingle {
            CryptoHelper.decryptData(
                salt,
                encDataString,
                ivDataString
            )
                .trim()
                .orNull()
        }
    }


    fun setPassword(password: String): Completable {
        return secretRepository.setPassword(password)
    }

    fun checkPassword(password: String): Completable {
        return secretRepository.checkPassword(password)
    }

    fun hasPassword(): Single<Boolean> {
        return secretRepository.hasPassword()
    }

    private fun getEntropy(): ByteArray {
        return ByteArray(ENTROPY_SIZE).also { seeds ->
            SecureRandom().nextBytes(seeds)
        }
    }

    companion object {
        const val PRIVATE_KEY_PREFIX = "0x"
        private const val ENTROPY_SIZE = 32
        private val MNEMONIC_SIZES = listOf(12, 16, 24)

        private const val PRIVATE_KEY = "PRIVATE_KEY"
        private const val MNEMONIC_KEY = "MNEMONIC_KEY"
    }
}