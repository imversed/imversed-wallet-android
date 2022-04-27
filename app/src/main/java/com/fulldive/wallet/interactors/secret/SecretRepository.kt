package com.fulldive.wallet.interactors.secret

import com.fulldive.wallet.di.modules.DefaultRepositoryModule
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@ProvidedBy(DefaultRepositoryModule::class)
class SecretRepository @Inject constructor(
    private val secretLocalSource: SecretLocalSource
) {

    fun checkPassword(password: String): Single<Boolean> {
        return secretLocalSource.checkPassword(password)
    }

    fun hasPassword(): Single<Boolean> {
        return secretLocalSource
            .getPassword()
            .map { true }
            .onErrorReturnItem(false)
    }

    fun setPassword(password: String): Completable {
        return secretLocalSource.setPassword(password)
    }
}