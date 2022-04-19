package com.fulldive.wallet.models.local

data class ApiHost(val url: String, val port: Int) {
    companion object {
        const val DEFAULT_PORT = 9090
        val EMPTY = ApiHost("", 0)

        fun from(text: String): ApiHost {
            return text.split(":".toRegex()).toTypedArray()
                .let { chunks ->
                    ApiHost(
                        chunks[0],
                        if (chunks.size > 1) chunks[1].toInt() else DEFAULT_PORT
                    )
                }
        }
    }
}