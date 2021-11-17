package com.gjn.easyapp.easyutils

import android.Manifest.permission.EXPAND_STATUS_BAR
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * 是否启动通知
 * */
fun Context.areNotificationsEnabled() =
    NotificationManagerCompat.from(this).areNotificationsEnabled()

/**
 * 发送通知
 * */
@SuppressLint("RestrictedApi")
fun Context.sendNotification(
    id: Int,
    tag: String? = null,
    config: NotificationChannelConfig = NotificationChannelConfig.getDefaultConfig(this),
    block: (NotificationCompat.Builder) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        notificationManager.createNotificationChannel(config.notificationCancel)
        println("createNotificationChannel ${config.notificationCancel}")
    }
    val notificationManager = NotificationManagerCompat.from(this)
    val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        println("Builder ${config.id}")
        NotificationCompat.Builder(this, config.id)
    } else {
        println("Builder null")
        NotificationCompat.Builder(this)
    }
    block.invoke(builder)
    notificationManager.notify(tag, id, builder.build())
}

/**
 * 发送前台通知
 * */
fun Service.sendForegroundNotification(
    id: Int,
    channelId: String = packageName,
    foregroundServiceType: Int? = null,
    block: (Notification.Builder) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val builder = Notification.Builder(this, channelId)
        block.invoke(builder)
        if (foregroundServiceType == null)
            startForeground(id, builder.build())
        else
            startForeground(id, builder.build(), foregroundServiceType)
    }
}

/**
 * 取消通知
 * */
fun Context.cancelNotification(id: Int, tag: String? = null) {
    NotificationManagerCompat.from(this).cancel(tag, id)
}

/**
 * 取消全部通知
 * */
fun Context.cancelAllNotification() {
    NotificationManagerCompat.from(this).cancelAll()
}

/**
 * 是否展开通知
 * */
@SuppressLint("WrongConstant")
@RequiresPermission(EXPAND_STATUS_BAR)
fun Context.setNotificationBarExpand(isExpand: Boolean) {
    val methodName = if (isExpand) "expandNotificationsPanel" else "collapsePanels"
    try {
        val service = getSystemService("statusbar")
        service.invokeMethod(methodName, "android.app.StatusBarManager", null)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 通知配置
 * @param id         频道id 必须唯一
 * @param name       频道名称
 * @param importance 频道重要性 来源{@link NotificationManager} e.g. NotificationManager.IMPORTANCE_DEFAULT
 * */
@RequiresApi(Build.VERSION_CODES.O)
class NotificationChannelConfig(val id: String, val name: CharSequence, importance: Int) {
    val notificationCancel = NotificationChannel(id, name, importance)

    fun setBypassDnd(bypassDnd: Boolean): NotificationChannelConfig {
        notificationCancel.setBypassDnd(bypassDnd)
        return this
    }

    fun setDescription(description: String): NotificationChannelConfig {
        notificationCancel.description = description
        return this
    }

    fun setGroup(groupId: String): NotificationChannelConfig {
        notificationCancel.group = groupId
        return this
    }

    fun setImportance(importance: Int): NotificationChannelConfig {
        notificationCancel.importance = importance
        return this
    }

    fun setLightColor(argb: Int): NotificationChannelConfig {
        notificationCancel.lightColor = argb
        return this
    }

    fun setLockScreenVisibility(lockScreenVisibility: Int): NotificationChannelConfig {
        notificationCancel.lockscreenVisibility = lockScreenVisibility
        return this
    }

    fun setName(name: CharSequence): NotificationChannelConfig {
        notificationCancel.name = name
        return this
    }

    fun setShowBadge(showBadge: Boolean): NotificationChannelConfig {
        notificationCancel.setShowBadge(showBadge)
        return this
    }

    fun setSound(sound: Uri, audioAttributes: AudioAttributes): NotificationChannelConfig {
        notificationCancel.setSound(sound, audioAttributes)
        return this
    }

    fun setVibrationPattern(vibrationPattern: LongArray): NotificationChannelConfig {
        notificationCancel.vibrationPattern = vibrationPattern
        return this
    }

    companion object {
        fun getDefaultConfig(context: Context) =
            NotificationChannelConfig(
                context.packageName,
                context.packageName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
    }
}