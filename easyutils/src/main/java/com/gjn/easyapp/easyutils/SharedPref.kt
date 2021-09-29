package com.gjn.easyapp.easyutils

import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class SharedPref<T>(private val context: Context) : ReadWriteProperty<Any?, T> {

    private var key: String? = null
    private var defValue: T? = null
    private var name: String = "${context.packageName}_sp"
    private var commit: Boolean = false

    constructor(
        context: Context,
        key: String,
        defValue: T,
        name: String = "${context.packageName}_sp",
        commit: Boolean = false
    ) : this(context) {
        this.key = key
        this.defValue = defValue
        this.name = name
        this.commit = commit
    }

    private val preferences by lazy { context.getSharedPreferences(name, Context.MODE_PRIVATE) }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        findPreference(key!!, defValue!!)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        putPreference(key!!, value)

    private fun findPreference(key: String, defValue: T): T = when (defValue) {
        is Int -> preferences.getInt(key, defValue)
        is Float -> preferences.getFloat(key, defValue)
        is Long -> preferences.getLong(key, defValue)
        is Boolean -> preferences.getBoolean(key, defValue)
        is String -> preferences.getString(key, defValue)
        else -> throw IllegalArgumentException("Unsupported type.")
    } as T

    private fun putPreference(key: String, value: T) {
        val edit = preferences.edit().run {
            when (value) {
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("Unsupported type.")
            }
        }
        if (commit) edit.commit() else edit.apply()
    }

    fun clearPref(): Boolean = preferences.edit().clear().commit()

    fun remove(key: String): Boolean = preferences.edit().remove(key).commit()
}