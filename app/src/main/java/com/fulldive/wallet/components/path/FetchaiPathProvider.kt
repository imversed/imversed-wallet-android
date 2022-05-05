package com.fulldive.wallet.components.path

import com.fulldive.wallet.extensions.or
import org.bitcoinj.crypto.ChildNumber

object FetchaiPathProvider : PathProvider(60) {
    private val pathMap: Map<Int, Int> = mapOf(0 to 118, 1 to 60, 2 to 60)

    override fun getPathList(customPath: Int): List<ChildNumber> {
        return mutableListOf(
            ChildNumber(44, true),
            ChildNumber(getPath(customPath), true),
        ).apply {
            if (customPath != 2) {
                add(ChildNumber.ZERO_HARDENED)
            }
            when (customPath) {
                0, 1 -> add(ChildNumber.ZERO)
                else -> Unit
            }
        }
    }

    override fun getPath(customPath: Int): Int {
        return pathMap[customPath].or(defaultPath)
    }

    override fun getPathString(path: Int, customPath: Int): String {
        return when (customPath) {
            0, 1 -> "m/44'/${getPath(customPath)}'/0'/0/$path"
            2 -> "m/44'/${getPath(customPath)}'/$path'/0/0"
            else -> "m/44'/${getPath(customPath)}'/0'/$path"
        }
    }
}
