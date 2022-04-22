package com.fulldive.wallet.interactors.secret

import android.content.Context
import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.extensions.safeSingle
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import wannabit.io.cosmostaion.appextensions.getPrivateSharedPreferences
import wannabit.io.cosmostaion.crypto.CryptoHelper
import javax.inject.Inject

@ProvidedBy(DefaultLocalStorageModule::class)
class SecretLocalSource @Inject constructor(
    context: Context
) {
    private var sharedPreferences = context.getPrivateSharedPreferences(KEY_CRYPTO)

    fun getPassword(): Single<String> {
        return safeSingle {
            sharedPreferences.getString(KEY_CRYPTO, null)
        }
    }

    fun checkPassword(password: String): Completable {
        return getPassword()
            .flatMapCompletable { encodedData ->
                safeCompletable {
                    if (!CryptoHelper.verifyData(
                            password,
                            encodedData,
                            KEY_CRYPTO_PASSWORD
                        )
                    ) {
                        throw InvalidPasswordException()
                    }
                }
            }
    }

    fun setPassword(password: String): Completable {
        return safeCompletable {
            CryptoHelper.signData(
                password,
                KEY_CRYPTO_PASSWORD
            )
                .let { encodedData ->
                    sharedPreferences.edit().putString(KEY_CRYPTO, encodedData).apply()
                }
        }
    }

    companion object {
        private const val KEY_CRYPTO = "crypto_wallet"
        private const val KEY_CRYPTO_PASSWORD = "crypto_password"
    }
}