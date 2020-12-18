package com.gjn.easyapp.easyutils

import android.graphics.Bitmap
import com.google.zxing.*
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QRCodeUtils {

    val defaultEncodeMap = mutableMapOf(
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H,
        EncodeHintType.CHARACTER_SET to "utf-8",
        EncodeHintType.MARGIN to 2
    )

    val defaultDecodeMap = mutableMapOf(
        DecodeHintType.CHARACTER_SET to "utf-8",
        DecodeHintType.TRY_HARDER to true,
        DecodeHintType.POSSIBLE_FORMATS to BarcodeFormat.QR_CODE
    )

    @JvmOverloads
    @Throws(WriterException::class)
    fun encode(
        code: String?,
        w: Int = 300,
        h: Int = 300,
        hints: Map<EncodeHintType, Any?>? = defaultEncodeMap
    ): BitMatrix = QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, w, h, hints)

    @Throws(
        FormatException::class,
        ChecksumException::class,
        NotFoundException::class
    )
    fun decodeToResult(
        binaryBitmap: BinaryBitmap?,
        hints: Map<DecodeHintType, Any?>? = defaultDecodeMap
    ): Result = QRCodeReader().decode(binaryBitmap, hints)

    @JvmOverloads
    fun decode(
        binaryBitmap: BinaryBitmap?,
        hints: Map<DecodeHintType, Any?>? = defaultDecodeMap
    ): String? = decodeToResult(binaryBitmap, hints).text

    @JvmOverloads
    fun bitMatrixToBitmap(
        bitMatrix: BitMatrix,
        positiveColor: Int = -0x1000000,
        negativeColor: Int = -0x1
    ): Bitmap {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        //将BitMatrix的像素保存下来
        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] = if (bitMatrix[x, y]) positiveColor else negativeColor
            }
        }
        //创建一样大小的Bitmap
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    fun drawLogo(
        qrCodeBitmap: Bitmap?,
        logoBitmap: Bitmap?,
        scale: Float = 0.2f
    ): Bitmap? {
        if (qrCodeBitmap == null || logoBitmap == null) return qrCodeBitmap
        val width = qrCodeBitmap.width
        val height = qrCodeBitmap.height
        val newWidth = (logoBitmap.width * scale).toInt()
        val newHeight = (logoBitmap.height * scale).toInt()
        val left = (width - newWidth) / 2f
        val top = (height - newHeight) / 2f
        val logoBmp = logoBitmap.scale(newWidth = newWidth, newHeight = newHeight)
        return qrCodeBitmap.drawBitmap(logoBmp, left, top)
    }

    fun bitmapToBinaryBitmap(bitmap: Bitmap?): BinaryBitmap? {
        if (bitmap == null) return null
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        //获取像素
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val source = RGBLuminanceSource(width, height, pixels)
        return BinaryBitmap(HybridBinarizer(source))
    }

    @JvmOverloads
    fun stringEncode(
        code: String?,
        w: Int = 300,
        h: Int = 300,
        hints: Map<EncodeHintType, Any?>? = defaultEncodeMap,
        positiveColor: Int = -0x1000000,
        negativeColor: Int = -0x1,
        logoBitmap: Bitmap? = null,
        scale: Float = 0.2f
    ): Bitmap? {
        if (code.isNullOrEmpty()) return null
        val bmp = bitMatrixToBitmap(encode(code, w, h, hints), positiveColor, negativeColor)
        return drawLogo(bmp, logoBitmap, scale)
    }

    fun bitmapDecode(bitmap: Bitmap?): String = decode(bitmapToBinaryBitmap(bitmap)) ?: ""
}