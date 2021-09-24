package com.gjn.easyapp.easyutils

import android.os.Parcelable
import android.util.Log
import com.tencent.mmkv.MMKV

class MMKVUtil private constructor(private val mmkv: MMKV) {

    fun encode(key: String, value: Any?) {
        when (value) {
            is String -> mmkv.encode(key, value)
            is Boolean -> mmkv.encode(key, value)
            is Int -> mmkv.encode(key, value)
            is Float -> mmkv.encode(key, value)
            is Double -> mmkv.encode(key, value)
            is Long -> mmkv.encode(key, value)
            is ByteArray -> mmkv.encode(key, value)
            is Parcelable -> mmkv.encode(key, value)
            else -> {
                Log.e(TAG, "$value encode fail.")
            }
        }
    }

    fun encodeStringSet(key: String, value: Set<String>) {
        mmkv.encode(key, value)
    }

    fun decodeString(key: String, defaultValue: String = ""): String =
        mmkv.decodeString(key, defaultValue)

    fun decodeBool(key: String, defaultValue: Boolean = false): Boolean =
        mmkv.decodeBool(key, defaultValue)

    fun decodeInt(key: String, defaultValue: Int = 0): Int = mmkv.decodeInt(key, defaultValue)

    fun decodeFloat(key: String, defaultValue: Float = 0f): Float =
        mmkv.decodeFloat(key, defaultValue)

    fun decodeDouble(key: String, defaultValue: Double = 0.0): Double =
        mmkv.decodeDouble(key, defaultValue)

    fun decodeLong(key: String, defaultValue: Long = 0): Long =
        mmkv.decodeLong(key, defaultValue)

    fun decodeBytes(key: String): ByteArray = mmkv.decodeBytes(key)

    fun decodeStringSet(key: String, defaultValue: Set<String> = emptySet()): Set<String> =
        mmkv.decodeStringSet(key, defaultValue)

    fun <T : Parcelable> decodeParcelable(key: String, clazz: Class<T>?): T? =
        mmkv.decodeParcelable<T>(key, clazz)

    fun hasKey(key: String): Boolean = mmkv.containsKey(key)

    fun removeKey(key: String) {
        mmkv.removeValueForKey(key)
    }

    fun removeKeys(keys: Array<String>) {
        mmkv.removeValuesForKeys(keys)
    }

    fun clearAll() {
        mmkv.clearAll()
    }

    companion object {
        private const val TAG = "MMKVUtil"

        private var mmkvUtil: MMKVUtil? = null

        fun initMMKV(mmkv: MMKV): MMKVUtil {
            if (mmkvUtil == null) {
                synchronized(MMKVUtil::class.java) {
                    if (mmkvUtil == null) {
                        mmkvUtil = MMKVUtil(mmkv)
                    }
                }
            }
            return mmkvUtil!!
        }
    }
}