package com.android.libs_common.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.live.common.constant.Constants

/**
 * Allows editing of this preference instance with a call to [apply][SharedPreferences.Editor.apply]
 * or [commit][SharedPreferences.Editor.commit] to persist the changes.
 * Default behaviour is [apply][SharedPreferences.Editor.apply].
 * ```
 * prefs.edit {
 *     putString("key", value)
 * }
 * ```
 * To [commit][SharedPreferences.Editor.commit] changes:
 * ```
 * prefs.edit(commit = true) {
 *     putString("key", value)
 * }
 * ```
 */
@SuppressLint("ApplySharedPref")
inline fun SharedPreferences.edit(
        commit: Boolean = true,
        action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}

fun Context.getBooleanBySp(key: String, name: String = Constants.SP_NAME, defaultValue: Boolean = false): Boolean {
    return getSharedPreferences(name, Context.MODE_PRIVATE)?.getBoolean(key, defaultValue)
            ?: defaultValue
}

fun Context.getIntBySp(key: String, name: String = Constants.SP_NAME, defaultValue: Int = 0): Int {
    return getSharedPreferences(name, Context.MODE_PRIVATE)?.getInt(key, defaultValue)
            ?: defaultValue
}

fun Context.getLongBySp(key: String, name: String = Constants.SP_NAME, defaultValue: Long = 0): Long {
    return getSharedPreferences(name, Context.MODE_PRIVATE)?.getLong(key, defaultValue)
            ?: defaultValue
}

fun Context.getStringBySp(key: String, name: String = Constants.SP_NAME, defaultValue: String = ""): String? {
    return getSharedPreferences(name, Context.MODE_PRIVATE)?.getString(key, defaultValue)
            ?: defaultValue
}

fun Fragment.getBooleanBySp(key: String, name: String = Constants.SP_NAME, defaultValue: Boolean = false): Boolean {
    return activity?.getSharedPreferences(name, Context.MODE_PRIVATE)?.getBoolean(key, defaultValue)
            ?: defaultValue
}

fun Fragment.getIntBySp(key: String, name: String = Constants.SP_NAME, defaultValue: Int = 0): Int {
    return activity?.getSharedPreferences(name, Context.MODE_PRIVATE)?.getInt(key, defaultValue)
            ?: defaultValue
}

fun Fragment.getLongBySp(key: String, name: String = Constants.SP_NAME, defaultValue: Long = 0): Long {
    return activity?.getSharedPreferences(name, Context.MODE_PRIVATE)?.getLong(key, defaultValue)
            ?: defaultValue
}

fun Fragment.getStringBySp(key: String, name: String = Constants.SP_NAME, defaultValue: String = ""): String {
    return activity?.getSharedPreferences(name, Context.MODE_PRIVATE)?.getString(key, defaultValue)
            ?: defaultValue
}