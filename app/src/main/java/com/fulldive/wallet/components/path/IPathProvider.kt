package com.fulldive.wallet.components.path

import org.bitcoinj.crypto.ChildNumber

interface IPathProvider {
    fun getPathList(customPath: Int): List<ChildNumber>
    fun getPathString(path: Int, customPath: Int): String
}