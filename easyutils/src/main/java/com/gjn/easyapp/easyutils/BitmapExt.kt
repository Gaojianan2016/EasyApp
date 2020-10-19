package com.gjn.easyapp.easyutils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import java.io.ByteArrayOutputStream
import java.io.File

@JvmOverloads
fun Bitmap.toByte(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 90
): ByteArray? {
    val outputStream = ByteArrayOutputStream()
    compress(format, quality, outputStream)
    return outputStream.toByteArray()
}

@JvmOverloads
fun Bitmap.compress(quality: Int = 90): Bitmap? {
    if (quality == 90) return this
    return toByte(quality = quality)?.toBitmap()
}

inline fun ByteArray.toBitmap(): Bitmap? = BitmapFactory.decodeByteArray(this, 0, size)

@JvmOverloads
fun File.toByte(quality: Int = 90): ByteArray? {
    return BitmapFactory.decodeFile(path).toByte(quality = quality)
}

@JvmOverloads
fun File.toBitmap(
    quality: Int = 90,
    maxWidth: Int? = null,
    maxHeight: Int? = null
): Bitmap? {
    when {
        !exists() -> return null
        maxWidth != null || maxHeight != null -> {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            var bitmap = BitmapFactory.decodeFile(path, options)
            var sampleSize = 2
            if (maxWidth != null) {
                val width = options.outWidth
                if (width > maxWidth) {
                    sampleSize = width / maxWidth
                    if (sampleSize <= 1) sampleSize = 2
                }
            } else if (maxHeight != null) {
                val height = options.outHeight
                if (height > maxHeight) {
                    sampleSize = height / maxHeight
                    if (sampleSize <= 1) sampleSize = 2
                }
            }
            options.inSampleSize = sampleSize
            options.inJustDecodeBounds = false
            bitmap = BitmapFactory.decodeFile(path, options)
            if (quality == 90) return bitmap
            return bitmap.toByte(quality = quality)?.toBitmap()
        }
        else -> {
            if (quality == 90) return BitmapFactory.decodeFile(path)
            return toByte(quality = quality)?.toBitmap()
        }
    }
}

@JvmOverloads
fun Bitmap.scale(
    ratio: Float = 1f,
    newWidth: Int? = null,
    newHeight: Int? = null
): Bitmap {
    when {
        newWidth != null && newHeight != null -> {
            val sx = newWidth / width.toFloat()
            val sy = newHeight / height.toFloat()
            val matrix = Matrix()
            matrix.postScale(sx, sy)
            return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
        }
        else -> {
            if (ratio == 1f) return this
            val matrix = Matrix()
            matrix.postScale(ratio, ratio)
            return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
        }
    }

}

@JvmOverloads
fun Bitmap.drawBitmap(
    bmp: Bitmap,
    bmpLeft: Float? = null,
    bmpTop: Float? = null
): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawBitmap(this, 0f, 0f, null)
    val left = bmpLeft ?: 0f
    val top = bmpTop ?: 0f
    canvas.drawBitmap(bmp, left, top, null)
    canvas.save()
    canvas.restore()
    if (bitmap.isRecycled) bitmap.recycle()
    return bitmap
}
