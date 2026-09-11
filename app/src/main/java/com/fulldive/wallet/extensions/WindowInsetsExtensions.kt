package com.fulldive.wallet.extensions

import android.app.Activity
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.max

/**
 * From Android 15 (targetSdk 35) every window is laid out edge to edge and there is no way to
 * opt out on targetSdk 36, so content draws underneath the status and navigation bars unless
 * the insets are applied by hand.
 *
 * Padding the content view reproduces what the app looked like before the enforcement: the
 * system bars sit over the window background, which is already black, and the content starts
 * below them.
 */
fun Activity.applySystemBarInsetsToContent() {
    findViewById<View>(android.R.id.content)?.applySystemBarInsets()
}

/**
 * Pads [this] by the system bars and any display cutout. The bottom also follows the keyboard,
 * which edge-to-edge windows no longer get from `adjustResize` alone.
 *
 * Insets are consumed so nested `fitsSystemWindows` views cannot apply them a second time.
 */
fun View.applySystemBarInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val bars = windowInsets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        val ime = windowInsets.getInsets(WindowInsetsCompat.Type.ime())
        view.setPadding(bars.left, bars.top, bars.right, max(bars.bottom, ime.bottom))
        WindowInsetsCompat.CONSUMED
    }
    ViewCompat.requestApplyInsets(this)
}

/**
 * Keeps a view clear of the navigation bar without touching the rest of the window, for screens
 * that deliberately draw their background edge to edge.
 */
fun View.applyBottomSystemBarInset() {
    val initialBottom = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val bars = windowInsets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        val ime = windowInsets.getInsets(WindowInsetsCompat.Type.ime())
        view.setPadding(
            view.paddingLeft,
            view.paddingTop,
            view.paddingRight,
            initialBottom + max(bars.bottom, ime.bottom)
        )
        windowInsets
    }
    ViewCompat.requestApplyInsets(this)
}
