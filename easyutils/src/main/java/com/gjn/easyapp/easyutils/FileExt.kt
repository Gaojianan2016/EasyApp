package com.gjn.easyapp.easyutils

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.*
import java.util.zip.ZipInputStream
import kotlin.jvm.Throws

fun String.file(): File = File(this)

fun Uri.file(): File? = File(path)

fun String.suffixToType(): String? = MimeTypeMap.getSingleton().getExtensionFromMimeType(this)

fun String.typeToSuffix(): String? = MimeTypeMap.getSingleton().getMimeTypeFromExtension(this)

fun String.suffix(): String = substring(lastIndexOf('.') + 1)

fun File.suffix(): String = name.suffix()

fun File.suffixToType(): String? = suffix().suffixToType()

fun File.typeToSuffix(): String? = suffix().typeToSuffix()

fun String.deleteFile(): Boolean = file().delete()

fun File.deleteFile(): Boolean {
    if (exists()) {
        if (isDirectory) {
            val children = list()
            for (child in children) {
                File(this, child).deleteFile()
            }
        }
        return delete()
    }
    return true
}

fun String.fileSize(): Long = file().fileSize()

fun File.fileSize(): Long {
    var size: Long = 0
    try {
        if (exists()) {
            for (file in listFiles()) {
                size += if (file.isDirectory) {
                    file.fileSize()
                } else {
                    file.length()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return size
}

fun File.create(): Boolean {
    if (!exists()) return mkdirs()
    return true
}

fun File.toBytes(): ByteArray? {
    var bytes: ByteArray? = null
    val fis: FileInputStream
    try {
        fis = FileInputStream(this)
        val bos = ByteArrayOutputStream()
        val b = ByteArray(1024)
        var n: Int
        while (fis.read(b).also { n = it } != -1) {
            bos.write(b, 0, n)
        }
        fis.close()
        bos.close()
        bytes = bos.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bytes
}

fun File.updateMediaStore(context: Context) {
    insertImage(context)
    scanFile(context)
}

/**
 * 通知相册插入缩略图 android 10之后Deprecated
 * 和scanFile一起使用 可以直接使用updateMediaStore
 * */
fun File.insertImage(context: Context) {
    MediaStore.Images.Media.insertImage(context.contentResolver, absolutePath, name, null)
}

/**
 * 通知相册更新图片 android 10之后Deprecated
 * 和insertImage一起使用 可以直接使用updateMediaStore
 * */
fun File.scanFile(context: Context) = this.uri().scanFile(context)

/**
 * 通知相册更新图片 android 10之后Deprecated
 * */
fun Uri.scanFile(context: Context) {
    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { it.data = this })
}

object FileUtils {

    private const val FILEPROVIDER = ".fileprovider"
    private const val APP = "/app"
    private const val DATA = "_data"

    fun openFile(context: Context, path: String) {
        openFile(context, path.file())
    }

    fun openFile(context: Context, file: File) {
        val mimeType = file.typeToSuffix()
        val uri = getFileUri(context, file)
        mimeType?.run {
            Intent(Intent.ACTION_VIEW).let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    it.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                } else {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                it.setDataAndType(uri, this)
                it.startActivity(context)
            }
        }
    }

    fun getFileUri(context: Context, path: String): Uri {
        return getFileUri(context, path.file())
    }

    fun getFileUri(context: Context, file: File): Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, context.packageName + FILEPROVIDER, file)
        } else {
            Uri.fromFile(file)
        }

    fun getFileFromUri(context: Context, uri: Uri?): File? {
        if (uri == null) return null
        return when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> getFileFormContentUri(context, uri)
            ContentResolver.SCHEME_FILE -> uri.file()
            else -> null
        }
    }

    fun getFileFormContentUri(context: Context, uri: Uri): File? {
        var filePath: String? = ""
        try {
            if (uri.authority!!.contains(context.packageName)) {
                filePath = uri.path!!.replace(APP, "")
            } else {
                val cursor = context.contentResolver.query(
                    uri, arrayOf(DATA), null, null, null
                )
                cursor?.run {
                    if (moveToFirst()) {
                        val index = getColumnIndex(DATA)
                        if (index != -1) filePath = getString(index)
                    }
                    close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return filePath?.file()
    }

    @Throws(IOException::class)
    fun unZip(
        context: Context,
        assetName: String,
        outputDirectory: String
    ) {
        // 创建解压目标目录
        var file = outputDirectory.file()
        // 如果目标目录不存在，则创建
        file.create()
        // 打开压缩文件
        val inputStream = context.assets.open(assetName)
        val zipInputStream = ZipInputStream(inputStream)
        // 读取一个进入点
        var zipEntry = zipInputStream.nextEntry
        // 使用1Mbuffer
        val buffer = ByteArray(1024 * 1024)
        // 解压时字节计数
        var count: Int
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            // 如果是一个目录
            if (zipEntry.isDirectory) {
                file = File(outputDirectory + File.separator + zipEntry.name)
                // 文件需要覆盖或者是文件不存在
                file.create()
            } else {
                // 如果是文件
                file = File(outputDirectory + File.separator + zipEntry.name)
                // 文件需要覆盖或者文件不存在，则解压文件
                if (!file.exists()) {
                    file.createNewFile()
                    val fileOutputStream = FileOutputStream(file)
                    while (zipInputStream.read(buffer).also { count = it } > 0) {
                        fileOutputStream.write(buffer, 0, count)
                    }
                    fileOutputStream.close()
                }
            }
            // 定位到下一个文件入口
            zipEntry = zipInputStream.nextEntry
        }
        zipInputStream.close()
    }
}