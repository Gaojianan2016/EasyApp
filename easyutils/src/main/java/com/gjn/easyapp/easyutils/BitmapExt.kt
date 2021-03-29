package com.gjn.easyapp.easyutils

import android.content.Context
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import kotlin.math.roundToInt

//compress
fun Bitmap.compress(quality: Int = 90): Bitmap? {
    if (quality == 90) return this
    return toByte(quality = quality)?.toBitmap()
}

//toByte
fun Bitmap.toByte(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 90
): ByteArray? {
    val outputStream = ByteArrayOutputStream()
    compress(format, quality, outputStream)
    return outputStream.toByteArray()
}

fun File.toByte(quality: Int = 90, opts: BitmapFactory.Options? = null) =
    this.toBitmap(opts = opts)?.toByte(quality = quality)

fun Int.toByte(context: Context, quality: Int = 90, opts: BitmapFactory.Options? = null) =
    this.toBitmap(context, opts = opts)?.toByte(quality = quality)

fun InputStream.toByte(
    quality: Int = 90,
    outPadding: Rect? = null,
    opts: BitmapFactory.Options? = null
) =
    this.toBitmap(outPadding = outPadding, opts = opts)?.toByte(quality = quality)

//toBitmap
fun ByteArray.toBitmap(quality: Int = 90, opts: BitmapFactory.Options? = null): Bitmap? =
    BitmapFactory.decodeByteArray(this, 0, size, opts).compress(quality = quality)

fun Int.toBitmap(
    context: Context,
    quality: Int = 90,
    opts: BitmapFactory.Options? = null
): Bitmap? =
    BitmapFactory.decodeResource(context.resources, this, opts).compress(quality = quality)

fun InputStream.toBitmap(
    quality: Int = 90,
    outPadding: Rect? = null,
    opts: BitmapFactory.Options? = null
): Bitmap? =
    BitmapFactory.decodeStream(this, outPadding, opts)?.compress(quality = quality)

fun File.toBitmap(quality: Int = 90, opts: BitmapFactory.Options? = null): Bitmap? =
    BitmapFactory.decodeFile(path, opts).compress(quality = quality)

fun File.toRectBitmap(
    quality: Int = 90,
    maxWidth: Int? = null,
    maxHeight: Int? = null
): Bitmap? {
    when {
        !exists() -> return null
        maxWidth.isNotNullOrZero() || maxWidth.isNotNullOrZero() -> {
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
            return this.toBitmap(quality, options)
        }
        else -> {
            return this.toBitmap(quality)
        }
    }
}

fun Bitmap.scale(ratio: Float): Bitmap {
    if (ratio == 1f) return this
    val matrix = Matrix().apply {
        postScale(ratio, ratio)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

fun Bitmap.scale(newWidth: Int, newHeight: Int): Bitmap {
    if (newWidth.isNullOrZero() || newHeight.isNullOrZero()) return this
    val sx = newWidth / width.toFloat()
    val sy = newHeight / height.toFloat()
    val matrix = Matrix().apply {
        postScale(sx, sy)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

fun Bitmap?.drawBitmap(
    bmp: Bitmap?,
    drawLeft: Float? = null,
    drawTop: Float? = null
): Bitmap? {
    if (this == null || bmp == null) return this
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawBitmap(this, 0f, 0f, null)
    val left = drawLeft ?: 0f
    val top = drawTop ?: 0f
    canvas.drawBitmap(bitmap, left, top, null)
    canvas.save()
    canvas.restore()
    if (bitmap.isRecycled) bitmap.recycle()
    return bitmap
}

/**
 * 缩放绘制迷你bitmap
 * gravity 目前只支持9种情况 不传或者超过都为5
 * 1 2 3
 * 4 5 6
 * 7 8 9
 * */
fun Bitmap?.drawMiniBitmap(
    miniBmp: Bitmap?,
    gravity: Int = 5,
    scale: Float = 0.2f
): Bitmap? {
    if (this == null || miniBmp == null) return this
    val newWidth = (miniBmp.width * scale).toInt()
    val newHeight = (miniBmp.height * scale).toInt()
    val bitmap = miniBmp.scale(newWidth = newWidth, newHeight = newHeight)
    val left: Float
    val top: Float
    when (gravity) {
        1 -> {
            left = 0f
            top = 0f
        }
        2 -> {
            left = (width - newWidth) / 2f
            top = 0f
        }
        3 -> {
            left = (width - newWidth).toFloat()
            top = 0f
        }
        4 -> {
            left = 0f
            top = (height - newHeight) / 2f
        }
        6 -> {
            left = (width - newWidth).toFloat()
            top = (height - newHeight) / 2f
        }
        7 -> {
            left = 0f
            top = (height - newHeight).toFloat()
        }
        8 -> {
            left = (width - newWidth) / 2f
            top = (height - newHeight).toFloat()
        }
        9 -> {
            left = (width - newWidth).toFloat()
            top = (height - newHeight).toFloat()
        }
        else -> {
            left = (width - newWidth) / 2f
            top = (height - newHeight) / 2f
        }
    }
    return drawBitmap(bitmap, left, top)
}

/**
 * @param context    上下文
 * @param blurRadius 模糊半径 1-25f
 * @param scaleSize  缩放比例 0.1-1f
 * */
fun Bitmap.blurBitmap(
    context: Context,
    blurRadius: Float = 13f,
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
    // 创建一个模糊效果的RenderScript的工具对象
    val rs = RenderScript.create(context)
    val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

    // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
    // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
    val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)

    // 设置渲染的模糊程度, 0.1f - 25f
    blurScript.setRadius(blurRadius.intervalOpen(0.1f, 25f))
    // 设置blurScript对象的输入内存
    blurScript.setInput(tmpIn)
    // 将输出数据保存到输出内存中
    blurScript.forEach(tmpOut)
    // 将数据填充到Allocation中
    tmpOut.copyTo(outputBitmap)
    return outputBitmap
}
