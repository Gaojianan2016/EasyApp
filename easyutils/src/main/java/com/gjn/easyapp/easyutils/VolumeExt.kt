package com.gjn.easyapp.easyutils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * 获取声音大小
 * @param streamType [
 *                    {@link AudioManager#STREAM_VOICE_CALL},
 *                    {@link AudioManager#STREAM_SYSTEM},
 *                    {@link AudioManager#STREAM_RING},
 *                    {@link AudioManager#STREAM_MUSIC},
 *                    {@link AudioManager#STREAM_ALARM},
 *                    {@link AudioManager#STREAM_NOTIFICATION},
 *                    {@link AudioManager#STREAM_DTMF},
 *                    {@link AudioManager#STREAM_ACCESSIBILITY}
 *                  ]
 * */
fun Context.getVolume(streamType: Int) = audioManager.getStreamVolume(streamType)

/**
 * 设置声音大小
 * @param streamType [
 *                    {@link AudioManager#STREAM_VOICE_CALL},
 *                    {@link AudioManager#STREAM_SYSTEM},
 *                    {@link AudioManager#STREAM_RING},
 *                    {@link AudioManager#STREAM_MUSIC},
 *                    {@link AudioManager#STREAM_ALARM},
 *                    {@link AudioManager#STREAM_NOTIFICATION},
 *                    {@link AudioManager#STREAM_DTMF},
 *                    {@link AudioManager#STREAM_ACCESSIBILITY}
 *                  ]
 * @param flags     [
 *                    {@link AudioManager#FLAG_SHOW_UI},
 *                    {@link AudioManager#FLAG_ALLOW_RINGER_MODES},
 *                    {@link AudioManager#FLAG_PLAY_SOUND},
 *                    {@link AudioManager#FLAG_REMOVE_SOUND_AND_VIBRATE},
 *                    {@link AudioManager#FLAG_VIBRATE}
 *                  ]
 * */
fun Context.setVolume(volume: Int, streamType: Int, flags: Int): Boolean =
    try {
        audioManager.setStreamVolume(streamType, volume, flags)
        true
    } catch (e: Exception) {
        false
    }

/**
 * 获取最大声音
 * @param streamType [
 *                    {@link AudioManager#STREAM_VOICE_CALL},
 *                    {@link AudioManager#STREAM_SYSTEM},
 *                    {@link AudioManager#STREAM_RING},
 *                    {@link AudioManager#STREAM_MUSIC},
 *                    {@link AudioManager#STREAM_ALARM},
 *                    {@link AudioManager#STREAM_NOTIFICATION},
 *                    {@link AudioManager#STREAM_DTMF},
 *                    {@link AudioManager#STREAM_ACCESSIBILITY}
 *                  ]
 * */
fun Context.getMaxVolume(streamType: Int) = audioManager.getStreamMaxVolume(streamType)

/**
 * 获取最小声音
 * @param streamType [
 *                    {@link AudioManager#STREAM_VOICE_CALL},
 *                    {@link AudioManager#STREAM_SYSTEM},
 *                    {@link AudioManager#STREAM_RING},
 *                    {@link AudioManager#STREAM_MUSIC},
 *                    {@link AudioManager#STREAM_ALARM},
 *                    {@link AudioManager#STREAM_NOTIFICATION},
 *                    {@link AudioManager#STREAM_DTMF},
 *                    {@link AudioManager#STREAM_ACCESSIBILITY}
 *                  ]
 * */
@RequiresApi(Build.VERSION_CODES.P)
fun Context.getMinVolume(streamType: Int) = audioManager.getStreamMinVolume(streamType)