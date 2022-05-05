package com.fulldive.wallet.components.path

import com.fulldive.wallet.extensions.or

class MultiPathProvider(
    defaultPath: Int,
    private val pathMap: Map<Int, Int>
) : PathProvider(defaultPath) {

    override fun getPath(customPath: Int): Int {
        return pathMap[customPath].or(defaultPath)
    }
}
