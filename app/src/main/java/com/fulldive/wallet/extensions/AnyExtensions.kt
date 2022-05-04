package com.fulldive.wallet.extensions

import android.os.Bundle
import wannabit.io.cosmostaion.utils.WLog
import kotlin.math.max
import kotlin.math.min

fun Any?.isNotNull(): Boolean {
    return null != this
}

fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY
fun Boolean?.orFalse() = this ?: false
fun Boolean?.orTrue() = this ?: true
inline fun <T> Boolean?.ifTrue(block: () -> T) = if (this == true) block.invoke() else null
inline fun <T> Boolean?.ifFalse(block: () -> T) = if (this == false) block.invoke() else null

fun <T> T?.or(value: T) = this ?: value
inline fun <T> T?.or(block: () -> T) = this ?: block.invoke()
inline fun <T, R> T?.letOr(block: (T) -> R, blockElse: () -> R): R {
    return if (this != null) block.invoke(this) else blockElse.invoke()
}

fun String?.orNull(): String? = if (this.isNullOrEmpty()) null else this
fun CharSequence?.orEmpty(): CharSequence = this ?: ""
fun CharSequence?.orEmptyString(): String = this.orEmpty().toString()

inline fun <R> safe(block: () -> R?): R? {
    return try {
        block.invoke()
    } catch (ex: Exception) {
        ex.printStackTrace()
        WLog.e(ex.toString())
        null
    }
}

inline fun <R> safe(block: () -> R?, defaultValue: R): R {
    return try {
        block.invoke().or(defaultValue)
    } catch (ex: Exception) {
        ex.printStackTrace()
        WLog.e(ex.toString())
        defaultValue
    }
}

inline fun <reified T> unsafeLazy(noinline initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer)

fun <A, B> combine(a: A, b: B) = a to b
fun <A, B, C> combine(a: A, b: B, c: C) = Triple(a, b, c)

fun <A> concat(a: List<A>, b: List<A>) = a + b

fun String.withPrefix(prefix: String) = if (startsWith(prefix)) this else "$prefix$this"

fun String.safeSubstring(startIndex: Int = 0, endIndex: Int = Integer.MAX_VALUE): String {
    val end: Int = max(0, min(length, endIndex))
    val start: Int = max(0, min(end, startIndex))
    return if (end <= start) "" else substring(start, end)
}
