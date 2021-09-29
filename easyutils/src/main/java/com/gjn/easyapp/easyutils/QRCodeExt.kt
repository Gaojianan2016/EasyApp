package com.gjn.easyapp.easyutils

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import androidx.annotation.ColorInt
import com.google.zxing.*
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.datamatrix.encoder.SymbolShapeHint
import com.google.zxing.pdf417.encoder.Compaction
import com.google.zxing.pdf417.encoder.Dimensions
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File

object QRCodeUtils {

    @Throws(WriterException::class)
    private fun encode(
        code: String?,
        w: Int,
        h: Int,
        hints: Map<EncodeHintType, Any?>?
    ): BitMatrix = QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, w, h, hints)

    @Throws(FormatException::class, ChecksumException::class, NotFoundException::class)
    private fun decodeToResult(
        binaryBitmap: BinaryBitmap?,
        hints: Map<DecodeHintType, Any?>?
    ): Result = QRCodeReader().decode(binaryBitmap, hints)

    private fun decode(
        binaryBitmap: BinaryBitmap?,
        hints: Map<DecodeHintType, Any?>?
    ): String? = decodeToResult(binaryBitmap, hints).text

    private fun bitMatrixToBitmap(
        bitMatrix: BitMatrix,
        @ColorInt positiveColor: Int,
        @ColorInt negativeColor: Int
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

    private fun drawLogo(
        qrCodeBitmap: Bitmap?,
        logoBitmap: Bitmap?,
        scale: Float = 0.2f
    ): Bitmap? {
        return qrCodeBitmap?.addImageWatermark(logoBitmap, scale = scale)
    }

    private fun bitmapToBinaryBitmap(bitmap: Bitmap?): BinaryBitmap? {
        if (bitmap == null) return null
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        //获取像素
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val source = RGBLuminanceSource(width, height, pixels)
        return BinaryBitmap(HybridBinarizer(source))
    }

    private fun MutableMap<EncodeHintType, Any?>.add(key: EncodeHintType, value: Any?) {
        if (value == null) return
        this[key] = value
    }

    private fun MutableMap<DecodeHintType, Any?>.add(key: DecodeHintType, value: Any?) {
        if (value == null) return
        this[key] = value
    }

    /**
     * 字符串加密
     * */
    fun stringEncode(
        code: String,
        builder: Builder = EncodeBuilder()
    ): Bitmap? {
        val hints: MutableMap<EncodeHintType, Any?> =
            mutableMapOf(EncodeHintType.CHARACTER_SET to builder.characterSet)
        var logo: Bitmap? = null
        var scale = 0f
        if (builder is EncodeBuilder) {
            hints.add(EncodeHintType.MARGIN, builder.margin)
            hints.add(EncodeHintType.ERROR_CORRECTION, builder.errorCorrectionLevel)

            hints.add(EncodeHintType.PDF417_COMPACT, builder.pdf417Compact)
            hints.add(EncodeHintType.PDF417_COMPACTION, builder.pdf417Compaction)
            hints.add(EncodeHintType.PDF417_DIMENSIONS, builder.pdf417Dimensions)
            hints.add(EncodeHintType.AZTEC_LAYERS, builder.aztecLayers)
            hints.add(EncodeHintType.QR_VERSION, builder.qrVersion)
            hints.add(EncodeHintType.DATA_MATRIX_SHAPE, builder.dataMatrixShape)
            hints.add(EncodeHintType.GS1_FORMAT, builder.gs1Format)

            logo = builder.logo
            scale = builder.scale
        }
        val bitmap = bitMatrixToBitmap(
            encode(code, builder.width, builder.height, hints),
            builder.positiveColor,
            builder.negativeColor
        )
        return if (logo == null) bitmap else drawLogo(bitmap, logo, scale)
    }

    /**
     * bitmap解密
     * */
    fun bitmapDecode(
        bitmap: Bitmap?,
        builder: Builder = DecodeBuilder()
    ): String {
        if (bitmap == null) return ""
        val hints: MutableMap<DecodeHintType, Any?> =
            mutableMapOf(DecodeHintType.CHARACTER_SET to builder.characterSet)
        if (builder is DecodeBuilder) {
            hints.add(DecodeHintType.TRY_HARDER, builder.tryHarder)
            hints.add(DecodeHintType.POSSIBLE_FORMATS, builder.possibleFormats)

            hints.add(DecodeHintType.OTHER, builder.other)
            hints.add(DecodeHintType.PURE_BARCODE, builder.pureBarcode)
            hints.add(DecodeHintType.ASSUME_CODE_39_CHECK_DIGIT, builder.assumeCode39CheckDigit)
            hints.add(DecodeHintType.ASSUME_GS1, builder.assumeGs1)
            hints.add(DecodeHintType.RETURN_CODABAR_START_END, builder.returnCodaBarStartEnd)
            hints.add(DecodeHintType.ALLOWED_LENGTHS, builder.allowedLengths)
            hints.add(DecodeHintType.ALLOWED_EAN_EXTENSIONS, builder.allowedEanExtensions)
            hints.add(DecodeHintType.NEED_RESULT_POINT_CALLBACK, builder.needResultPointCallback)
        }
        return decode(bitmapToBinaryBitmap(bitmap), hints) ?: ""
    }

    open class Builder {
        var width = 300
        var height = 300
        var characterSet = "utf-8"
        var positiveColor = Color.BLACK
        var negativeColor = Color.WHITE
    }

    class EncodeBuilder : Builder() {
        var logo: Bitmap? = null
        var scale = 0.2f

        var margin = 2
        var errorCorrectionLevel = ErrorCorrectionLevel.Q

        var pdf417Compact: Boolean? = null
        var pdf417Compaction: Compaction? = null
        var pdf417Dimensions: Dimensions? = null
        var aztecLayers: Int? = null
        var qrVersion: Int? = null
        var dataMatrixShape: SymbolShapeHint? = null
        var gs1Format: String? = null
    }

    class DecodeBuilder : Builder() {
        var tryHarder: Boolean? = true
        var possibleFormats = BarcodeFormat.QR_CODE

        var other: Any? = null
        var pureBarcode: Boolean? = null
        var assumeCode39CheckDigit: Boolean? = null
        var assumeGs1: Boolean? = null
        var returnCodaBarStartEnd: Boolean? = null
        var allowedLengths: IntArray? = null
        var allowedEanExtensions: IntArray? = null
        var needResultPointCallback: ResultPointCallback? = null
    }
}

/**
 * 字符串加密成二维码bitmap
 * */
fun String.encodeQrCode(builder: QRCodeUtils.EncodeBuilder = QRCodeUtils.EncodeBuilder()) =
    QRCodeUtils.stringEncode(this, builder)

/**
 * 二维码bitmap解密成字符串
 * */
fun Bitmap.decodeQrCode(builder: QRCodeUtils.DecodeBuilder = QRCodeUtils.DecodeBuilder()) =
    QRCodeUtils.bitmapDecode(this, builder)

/**
 * ImageView设置二维码ImageBitmap
 * */
fun ImageView.setQrCodeImageBitmap(
    code: String,
    builder: QRCodeUtils.EncodeBuilder = QRCodeUtils.EncodeBuilder()
) {
    setImageBitmap(code.encodeQrCode(builder))
}

/**
 * 获取ImageView二维码解析字符串
 * */
fun ImageView.getQrCodeByBitmap(builder: QRCodeUtils.DecodeBuilder = QRCodeUtils.DecodeBuilder()) =
    toBitmap().decodeQrCode(builder)

/**
 * 获取文件二维码解析字符串
 * */
fun File.getQrCode(builder: QRCodeUtils.DecodeBuilder = QRCodeUtils.DecodeBuilder()) =
    toRectBitmap()?.decodeQrCode(builder)
