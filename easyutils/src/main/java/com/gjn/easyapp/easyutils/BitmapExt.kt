package com.gjn.easyapp.easyutils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.app.ActivityCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import kotlin.math.roundToInt

//compress
fun Bitmap.compress(quality: Int = 90): Bitmap? {
    if (quality == 90) return this
    return toByte(quality = quality)?.toBitmap()
}

//toDrawable
fun Bitmap.toDrawable(context: Context) = BitmapDrawable(context.resources, this)

fun ByteArray.toDrawable(context: Context) = toBitmap()?.toDrawable(context)

//toByte
fun Bitmap.toByte(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 90
): ByteArray? {
    return ByteArrayOutputStream().also {
        compress(format, quality, it)
    }.toByteArray()
}

fun File.toByte(quality: Int = 90, opts: BitmapFactory.Options? = null): ByteArray? {
    if (!exists()) return null
    return toBitmap(opts = opts)?.toByte(quality = quality)
}

fun Int.toByte(context: Context, quality: Int = 90, opts: BitmapFactory.Options? = null) =
    toBitmap(context, opts = opts)?.toByte(quality = quality)

fun InputStream.toByte(
    quality: Int = 90,
    outPadding: Rect? = null,
    opts: BitmapFactory.Options? = null
) = toBitmap(outPadding = outPadding, opts = opts)?.toByte(quality = quality)

//toBitmap
fun ByteArray.toBitmap(quality: Int = 90, opts: BitmapFactory.Options? = null) =
    BitmapFactory.decodeByteArray(this, 0, size, opts).compress(quality = quality)

fun Int.toBitmap(
    context: Context,
    quality: Int = 90,
    opts: BitmapFactory.Options? = null
) = BitmapFactory.decodeResource(context.resources, this, opts).compress(quality = quality)

fun Drawable.toBitmap(): Bitmap? {
    if (this is BitmapDrawable) return bitmap ?: null
    val w = if (intrinsicWidth <= 0) 1 else intrinsicWidth
    val h = if (intrinsicHeight <= 0) 1 else intrinsicHeight
    val config = Bitmap.Config.ARGB_8888
    return Bitmap.createBitmap(w, h, config).apply {
        Canvas(this).let { canvas ->
            setBounds(0, 0, canvas.width, canvas.height)
            draw(canvas)
        }
    }
}

fun InputStream.toBitmap(
    quality: Int = 90,
    outPadding: Rect? = null,
    opts: BitmapFactory.Options? = null
) = BitmapFactory.decodeStream(this, outPadding, opts)?.compress(quality = quality)

fun File.toBitmap(quality: Int = 90, opts: BitmapFactory.Options? = null): Bitmap? {
    if (!exists()) return null
    return BitmapFactory.decodeFile(path, opts).compress(quality = quality)
}

/**
 * 向量图转bitmap
 * */
fun Int.vectorToBitmap(context: Context): Bitmap? =
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        val drawable = ActivityCompat.getDrawable(context, this)
        if (drawable == null) {
            null
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            ).apply {
                val canvas = Canvas(this)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
            }
        }
    } else {
        toBitmap(context)
    }

fun File.toRectBitmap(
    quality: Int = 90,
    newWidth: Int? = null,
    newHeight: Int? = null,
    recycle: Boolean = false
): Bitmap? {
    if (!exists()) return null
    if (newWidth.isNullOrZero() || newHeight.isNullOrZero()) return toBitmap(quality)
    return toBitmap(quality)?.scale(newWidth!!, newHeight!!, recycle)
}

fun View.toBitmap(): Bitmap {
    val enabled = isDrawingCacheEnabled
    val drawing = willNotCacheDrawing()
    isDrawingCacheEnabled = true
    setWillNotCacheDrawing(false)
    val bitmap: Bitmap
    if (drawingCache == null || drawingCache.isRecycled) {
        val spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        measure(spec, spec)
        layout(0, 0, measuredWidth, measuredHeight)
        buildDrawingCache()
        if (drawingCache == null || drawingCache.isRecycled) {
            bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.RGB_565)
            draw(Canvas(bitmap))
        } else {
            bitmap = Bitmap.createBitmap(drawingCache)
        }
    } else {
        bitmap = Bitmap.createBitmap(drawingCache)
    }
    isDrawingCacheEnabled = enabled
    setWillNotCacheDrawing(drawing)
    return bitmap
}

///////////////////////////////////////////////////////////////////////////
// Bitmap operate
///////////////////////////////////////////////////////////////////////////

fun Bitmap.scale(
    sx: Float,
    sy: Float,
    recycle: Boolean = false
): Bitmap {
    if (sx == 1f && sy == 1f) return this
    val bitmap = Bitmap.createBitmap(
        this, 0, 0, width, height,
        Matrix().apply { postScale(sx, sy) }, true
    )
    if (recycle && !isRecycled && bitmap != this) recycle()
    return bitmap
}

fun Bitmap.scale(
    ratio: Float,
    recycle: Boolean = false
) = scale(ratio, ratio, recycle)

fun Bitmap.scale(
    newWidth: Int,
    newHeight: Int,
    recycle: Boolean = false
): Bitmap {
    if (newWidth == 0 || newHeight == 0) return this
    return scale(newWidth / width.toFloat(), newHeight / height.toFloat(), recycle)
}

fun Bitmap.clip(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    recycle: Boolean = false
): Bitmap {
    if (x == 0 && y == 0 && width == this.width && height == this.height) return this
    val bitmap = Bitmap.createBitmap(this, x, y, width, height)
    if (recycle && !isRecycled && bitmap != this) recycle()
    return bitmap
}

fun Bitmap.skew(
    kx: Float,
    ky: Float,
    px: Float = 0f,
    py: Float = 0f,
    recycle: Boolean = false
): Bitmap {
    if (kx == 0f && ky == 0f) return this
    val bitmap = Bitmap.createBitmap(
        this, 0, 0, width, height,
        Matrix().apply { setSkew(kx, ky, px, py) }, true
    )
    if (recycle && !isRecycled && bitmap != this) recycle()
    return bitmap
}

fun Bitmap.rotate(
    degrees: Float,
    px: Float = 0f,
    py: Float = 0f,
    recycle: Boolean = false
): Bitmap {
    if (degrees == 0f) return this
    val bitmap = Bitmap.createBitmap(
        this, 0, 0, width, height,
        Matrix().apply { setRotate(degrees, px, py) }, true
    )
    if (recycle && !isRecycled && bitmap != this) recycle()
    return bitmap
}

fun Bitmap.alpha(recycle: Boolean = false): Bitmap {
    val bitmap = extractAlpha()
    if (recycle && !isRecycled && bitmap != this) recycle()
    return bitmap
}

fun Bitmap.gray(recycle: Boolean = false): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, config)
    val paint = Paint()
    val colorMatrix = ColorMatrix().apply {
        setSaturation(0f)
    }
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    Canvas(bitmap).drawBitmap(this, 0f, 0f, paint)
    if (recycle && !isRecycled && bitmap != this) recycle()
    return bitmap
}

/**
 * 切圆
 * */
fun Bitmap.toCircle(
    @IntRange(from = 0) borderSize: Int = 0,
    @ColorInt borderColor: Int = 0,
    recycle: Boolean = false
): Bitmap {
    val size = width.coerceAtMost(height)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val bitmap = Bitmap.createBitmap(width, height, config)
    //取中心和 中心RectF
    val center = size / 2f
    val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat()).apply {
        inset((width - size) / 2f, (height - size) / 2f)
    }
    //绘制Bitmap
    val matrix = Matrix().apply {
        setTranslate(rectF.left, rectF.top)
        if (width != height) {
            preScale(size / width.toFloat(), size / height.toFloat())
        }
    }
    paint.shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP).apply {
        setLocalMatrix(matrix)
    }
    val canvas = Canvas(bitmap).apply {
        drawRoundRect(rectF, center, center, paint)
    }
    //绘制边框
    if (borderSize > 0) {
        paint.shader = null
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderSize.toFloat()
        canvas.drawCircle(width / 2f, height / 2f, center - borderSize / 2f, paint)
    }
    if (recycle && !isRecycled && bitmap != this) recycle()
    return bitmap
}

/**
 * 切圆角
 * */
fun Bitmap.toRoundCorner(
    radius: Float,
    @IntRange(from = 0) borderSize: Int = 0,
    @ColorInt borderColor: Int = 0,
    recycle: Boolean = false
) = toRoundCorner(
    floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius),
    borderSize,
    borderColor,
    recycle
)

/**
 * 切圆角
 * @param radii 8个值的数组，4对[X,Y]半径
 * */
fun Bitmap.toRoundCorner(
    radii: FloatArray,
    @IntRange(from = 0) borderSize: Int = 0,
    @ColorInt borderColor: Int = 0,
    recycle: Boolean = false
): Bitmap {
    if (radii.isEmpty()) return this
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val bitmap = Bitmap.createBitmap(width, height, config)
    //取绘制矩形和圆角路径
    val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat()).apply {
        inset(borderSize / 2f, borderSize / 2f)
    }
    val path = Path().apply {
        addRoundRect(rectF, radii, Path.Direction.CW)
    }
    //绘制Bitmap
    paint.shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    val canvas = Canvas(bitmap).apply {
        drawPath(path, paint)
    }
    //绘制边框
    if (borderSize > 0) {
        paint.shader = null
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderSize.toFloat()
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawPath(path, paint)
    }
    if (recycle && !isRecycled && bitmap != this) recycle()
    return bitmap
}

/**
 * 添加文字水印
 * */
fun Bitmap.addTextWatermark(
    text: CharSequence,
    textSize: Float = 24f,
    @ColorInt color: Int = Color.RED,
    x: Float = 0f,
    y: Float = 0f,
    degrees: Float = 0f,
    @IntRange(from = 0, to = 255) alpha: Int = 255,
    recycle: Boolean = false
): Bitmap {
    if (text.isEmpty()) return this
    val bitmap = copy(config, true)
    val textPaint = TextPaint().apply {
        this.color = color
        this.alpha = alpha
        this.textSize = textSize
    }
    Canvas(bitmap).apply {
        rotate(degrees, x, y)
        translate(x, y)
    }.drawStaticLayout(text, textPaint)
    if (recycle && !isRecycled && bitmap != this) recycle()
    return bitmap
}

private fun Canvas.drawStaticLayout(text: CharSequence, textPaint: TextPaint) {
    val textLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        StaticLayout.Builder.obtain(text, 0, text.length, textPaint, width).build()
    } else {
        StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
    }
    textLayout.draw(this)
}

/**
 * 添加图片水印
 * @param gravity [0..9]之间
 * 位置入下
 * 1 2 3
 * 4 5 6
 * 7 8 9
 * */
fun Bitmap.addImageWatermark(
    mark: Bitmap?,
    @IntRange(from = 1, to = 9) gravity: Int = 5,
    @FloatRange(from = 0.1, to = 1.0) scale: Float = 0.2f,
    @IntRange(from = 0, to = 255) alpha: Int = 255,
    degrees: Float = 0f,
    recycle: Boolean = false
): Bitmap {
    if (mark == null) return this
    val markWidth = (mark.width * scale).toInt()
    val markHeight = (mark.height * scale).toInt()
    val markBitmap = mark.scale(markWidth, markHeight)
    val left = when (gravity % 3) {
        1 -> 0f
        2 -> (width - markWidth) / 2f
        else -> (width - markHeight).toFloat()
    }
    val top = when {
        gravity > 6 -> (height - markHeight).toFloat()
        gravity > 3 -> (height - markHeight) / 2f
        else -> 0f
    }
    return addImageWatermark(markBitmap, left, top, degrees, alpha, recycle)
}

/**
 * 添加图片水印
 * */
fun Bitmap.addImageWatermark(
    mark: Bitmap?,
    left: Float = 0f,
    top: Float = 0f,
    degrees: Float = 0f,
    @IntRange(from = 0, to = 255) alpha: Int = 255,
    recycle: Boolean = false
): Bitmap {
    if (mark == null) return this
    val bitmap = copy(config, true)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.alpha = alpha
    }
    Canvas(bitmap).run {
        rotate(degrees, left, top)
        drawBitmap(mark, left, top, paint)
    }
    if (recycle && !isRecycled && bitmap != this) recycle()
    return bitmap
}

/**
 * 快速模糊
 * */
fun Bitmap.fastBlur(
    context: Context,
    @FloatRange(from = 0.0, to = 1.0, fromInclusive = false) scale: Float = 0.8f,
    @FloatRange(from = 0.1, to = 25.0, fromInclusive = false) blurRadius: Float = 13f,
    recycle: Boolean = false
): Bitmap {
    if (scale == 0f) return this
    // 计算图片缩小后的长宽
    val width = (width * scale).roundToInt()
    val height = (height * scale).roundToInt()
    // 将缩小后的图片做为预渲染的图片
    val inputBitmap = Bitmap.createScaledBitmap(this, width, height, false)
    // 创建一张渲染后的输出图片
    val outputBitmap = Bitmap.createBitmap(inputBitmap)
    // 创建RenderScript内核对象
    // 创建一个模糊效果的RenderScript的工具对象
    val rs = RenderScript.create(context)
    // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
    // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
    val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
    ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)).run {
        // 设置渲染的模糊程度, 0.1f - 25f
        setRadius(blurRadius.intervalOpen(0.1f, 25f))
        // 设置blurScript对象的输入内存
        setInput(tmpIn)
        // 将输出数据保存到输出内存中
        forEach(tmpOut)
    }
    // 将数据填充到Allocation中
    tmpOut.copyTo(outputBitmap)
    if (recycle && !isRecycled && outputBitmap != this) recycle()
    return outputBitmap
}

/**
 * 获取图片文件旋转角度
 * */
fun File.getRotateDegree(): Int {
    if (!exists()) return -1
    try {
        val orientation = ExifInterface(path).getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return -1
}