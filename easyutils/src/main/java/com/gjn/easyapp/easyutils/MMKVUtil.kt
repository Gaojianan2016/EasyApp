package com.gjn.easyapp.easyutils

import android.os.Parcelable
import com.tencent.mmkv.MMKV

class MMKVUtil private constructor(private val mmkv: MMKV) {

    @Suppress("UNCHECKED_CAST")
    fun encode(pair: Pair<String, Any?>) {
        val key = pair.first
        val value = pair.second ?: return
        when (value) {
            is String -> mmkv.encode(key, value)

            // Scalars
            is Boolean -> mmkv.encode(key, value)
            is Double -> mmkv.encode(key, value)
            is Float -> mmkv.encode(key, value)
            is Int -> mmkv.encode(key, value)
            is Long -> mmkv.encode(key, value)

            // References
            is Parcelable -> mmkv.encode(key, value)

            // Scalar arrays
            is ByteArray -> mmkv.encode(key, value)

            // Reference set
            is Set<*> -> {
                val componentType = value.javaClass.componentType!!
                when {
                    String::class.java.isAssignableFrom(componentType) -> {
                        mmkv.encode(key, value as Set<String>)
                    }
                    else -> {
                        val valueType = componentType.canonicalName
                        throw IllegalArgumentException(
                            "Illegal value set type $valueType for key \"$key\""
                        )
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun decode(key: String, defaultValue: Any?): Any? {
        if (key.isEmpty() || defaultValue == null) return null

        return when (defaultValue) {
            // Scalars
            is Boolean -> mmkv.decodeBool(key, defaultValue)
            is Double -> mmkv.decodeDouble(key, defaultValue)
            is Float -> mmkv.decodeFloat(key, defaultValue)
            is Int -> mmkv.decodeInt(key, defaultValue)
            is Long -> mmkv.decodeLong(key, defaultValue)

            // References
            is Parcelable -> mmkv.decodeParcelable(key, defaultValue as Class<Parcelable>?)

            // Scalar arrays
            is ByteArray -> mmkv.decodeBytes(key, defaultValue)

            // Reference set
            is Set<*> -> {
                val componentType = defaultValue.javaClass.componentType!!
                if (String::class.java.isAssignableFrom(componentType)) {
                    mmkv.decodeStringSet(key, defaultValue as Set<String>)
                } else {
                    val valueType = componentType.canonicalName
                    throw IllegalArgumentException(
                        "Illegal value get type $valueType for key \"$key\""
                    )
                }
            }

            else -> null
        }
    }

    fun decodeString(key: String, defaultValue: String = ""): String =
        mmkv.decodeString(key, defaultValue)

    fun decodeBool(key: String, defaultValue: Boolean = false): Boolean =
        mmkv.decodeBool(key, defaultValue)

    fun decodeInt(key: String, defaultValue: Int = 0): Int =
        mmkv.decodeInt(key, defaultValue)

    fun decodeFloat(key: String, defaultValue: Float = 0f): Float =
        mmkv.decodeFloat(key, defaultValue)

    fun decodeDouble(key: String, defaultValue: Double = 0.0): Double =
        mmkv.decodeDouble(key, defaultValue)

    fun decodeLong(key: String, defaultValue: Long = 0): Long =
        mmkv.decodeLong(key, defaultValue)

    fun decodeBytes(key: String): ByteArray =
        mmkv.decodeBytes(key)

    fun decodeStringSet(key: String, defaultValue: Set<String> = emptySet()): Set<String> =
        mmkv.decodeStringSet(key, defaultValue)

    fun <T : Parcelable> decodeParcelable(key: String, clazz: Class<T>?): T? =
        mmkv.decodeParcelable(key, clazz)

    fun containsKey(key: String): Boolean = mmkv.containsKey(key)

    fun removeKey(key: String) {
        mmkv.removeValueForKey(key)
    }

    fun removeKeys(keys: Array<String>) {
        mmkv.removeValuesForKeys(keys)
    }

    fun clearAll() {
        mmkv.clearAll()
    }

    companion object : SingletonCompanionImpl<MMKVUtil, MMKV>(::MMKVUtil)
}