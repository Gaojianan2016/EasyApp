package com.gjn.easyapp.easyutils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.roundToInt

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

@JvmOverloads
        /**
         * @param context    上下文
         * @param blurRadius 模糊半径 1-25f
         * @param scaleSize  缩放比例
         * */
fun Bitmap.blurBitmap(
    context: Context,
    blurRadius: Float = 12f,
    scaleSize: Float = 1f
): Bitmap {
    // 计算图片缩小后的长宽
    val width = (width * scaleSize).roundToInt()
    val height = (height * scaleSize).roundToInt()
    // 将缩小后的图片做为预渲染的图片
    val inputBitmap = Bitmap.createScaledBitmap(this, width, height, false)
    // 创建一张渲染后的输出图片
    val outputBitmap = Bitmap.createBitmap(inputBitmap)
    // 创建RenderScript内核对象
    val rs = RenderScript.create(context)
    // 创建一个模糊效果的RenderScript的工具对象
    val blurScript =
        ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
    // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
    val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
    // 设置渲染的模糊程度, 25f是最大模糊度
    blurScript.setRadius(blurRadius)
    // 设置blurScript对象的输入内存
    blurScript.setInput(tmpIn)
    // 将输出数据保存到输出内存中
    blurScript.forEach(tmpOut)
    // 将数据填充到Allocation中
    tmpOut.copyTo(outputBitmap)
    return outputBitmap
}
