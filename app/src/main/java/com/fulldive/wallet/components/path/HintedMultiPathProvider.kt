package com.fulldive.wallet.components.path

import com.fulldive.wallet.extensions.or

class HintedMultiPathProvider(
    defaultPath: Int,
    private val pathMap: Map<Int, Int>,
    private val hint: String,
    private val hintMap: Map<Int, String>,
) : PathProvider(defaultPath) {

    override fun getPathString(path: Int, customPath: Int): String {
        return super.getPathString(path, customPath).let { text ->
            val hintText = hintMap[customPath].or(hint)
            if (hintText.isNotEmpty()) {
                "$text ($hintText)"
            } else {
                text
            }
        }
    }

    override fun getPath(customPath: Int): Int {
        return pathMap[customPath].or(defaultPath)
    }
}
