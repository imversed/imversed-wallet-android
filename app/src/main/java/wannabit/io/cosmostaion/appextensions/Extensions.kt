package wannabit.io.cosmostaion.appextensions

import android.content.Context
import android.content.SharedPreferences

fun Context.getPrivateSharedPreferences(key: String = "preference"): SharedPreferences {
    return this.getSharedPreferences(packageName + "_$key", Context.MODE_PRIVATE)
}
