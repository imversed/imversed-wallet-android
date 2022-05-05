package com.fulldive.wallet.components.path

import org.bitcoinj.crypto.ChildNumber

open class PathProvider(
    protected val defaultPath: Int
) : IPathProvider {

    override fun getPathList(customPath: Int): List<ChildNumber> {
        return mutableListOf(
            ChildNumber(44, true),
            ChildNumber(getPath(customPath), true),
            ChildNumber.ZERO_HARDENED,
            ChildNumber.ZERO
        )
    }

    override fun getPathString(path: Int, customPath: Int): String {
        return "m/44'/${getPath(customPath)}'/0'/0/$path"
    }

    protected open fun getPath(customPath: Int): Int {
        return defaultPath
    }

    companion object {
        val DEFAULT = PathProvider(118)
    }
}
