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

fun String.file(): File = File(this)

fun Uri.file(): File = File(path)

fun String.suffix(): String = substring(lastIndexOf('.') + 1)

fun File.suffix(): String = name.suffix()

fun String.suffixFindType(): String? = MimeTypeMap.getSingleton().getExtensionFromMimeType(this)

fun String.typeFindSuffix(): String? = MimeTypeMap.getSingleton().getMimeTypeFromExtension(this)

fun File.suffixFindType(): String? = suffix().suffixFindType()

fun File.typeFindSuffix(): String? = suffix().typeFindSuffix()

/**
 * 文件重命名
 * @param newName 新名字
 * */
fun File?.rename(newName: String): Boolean {
    if (this == null || newName.isEmpty()) return false
    if (!exists()) return false
    if (name.isEmpty()) return false
    if (newName == name) return true
    val newFile = "$parent${File.separator}$newName".file()
    return !newFile.exists() && this.renameTo(newFile)
}

/**
 * 判断是否是文件夹
 * */
fun File?.isExistsDir() = this != null && exists() && isDirectory

/**
 * 判断是否是文件
 * */
fun File?.isExistsFile() = this != null && exists() && isFile

/**
 * 创建文件夹(存在不处理)
 * */
fun String.createOrExistsDir() = file().createOrExistsDir()

/**
 * 创建文件夹(存在不处理)
 * */
fun File?.createOrExistsDir(): Boolean {
    if (this == null) return false
    return if (exists()) isDirectory else mkdirs()
}

/**
 * 创建父目录
 * */
fun File?.createParentDir(): Boolean {
    if (this == null) return false
    return this.parentFile.createOrExistsDir()
}

/**
 * 创建文件(存在不处理)
 * */
fun String.createOrExistsFile() = file().createOrExistsFile()

/**
 * 创建文件(存在不处理)
 * */
fun File?.createOrExistsFile(): Boolean {
    if (this == null) return false
    if (exists()) return isFile
    if (!createParentDir()) return false
    try {
        return createNewFile()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * 创建新文件(存在删除)
 * */
fun String.createFile() = file().createFile()

/**
 * 创建新文件(存在删除)
 * */
fun File?.createFile(): Boolean {
    if (this == null) return false
    //存在 删除失败 返回失败
    if (exists() && !delete()) return false
    if (!createOrExistsFile()) return false
    try {
        return createNewFile()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * 复制文件
 * @param targetPath 目标路径
 * */
fun File?.copyToPath(targetPath: String): Boolean {
    if (this == null || targetPath.isEmpty()) return false
    return if (isDirectory) copyOrMoveDir(targetPath) else copyOrMoveFile(targetPath.file())
}

/**
 * 移动文件
 * @param targetPath 目标路径
 * */
fun File?.moveToPath(targetPath: String): Boolean {
    if (this == null || targetPath.isEmpty()) return false
    return if (isDirectory) copyOrMoveDir(targetPath, true)
    else copyOrMoveFile(targetPath.file(), true)
}

/**
 * 复制或者移动文件夹
 * @param targetDirPath 目标路径
 * @param isMove     true 移动 false 复制
 * */
fun File?.copyOrMoveDir(targetDirPath: String, isMove: Boolean = false): Boolean {
    if (this == null || targetDirPath.isEmpty()) return false
    val srcPath = path + File.separator
    val destPath =
        if (targetDirPath.endsWith(File.separator)) targetDirPath else targetDirPath + File.separator
    //目标文件夹判断
    if (destPath.contains(srcPath)) return false
    if (!exists() || !isDirectory) return false
    if (!destPath.createOrExistsDir()) return false

    listFiles()?.forEach {
        val tempFile = "$destPath${it.name}".file()
        if (it.isFile) {
            if (!it.copyOrMoveFile(tempFile, isMove)) return false
        } else if (it.isDirectory) {
            if (!it.copyOrMoveDir(tempFile.path, isMove)) return false
        }
    }
    return !isMove || delete()
}

/**
 * 复制或者移动文件
 * @param targetFile 目标文件
 * @param isMove     true 移动 false 复制
 * */
fun File?.copyOrMoveFile(targetFile: File?, isMove: Boolean = false): Boolean {
    if (this == null || targetFile == null) return false
    //目标文件判断
    if (this == targetFile) return false
    if (!exists() || !isFile) return false
    if (targetFile.exists()) {
        if (!targetFile.delete()) {
            return false
        }
    }
    if (!targetFile.createParentDir()) return false
    try {
        //todo 复制or移动文件
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}


/**
 * 删除文件夹
 * */
fun String.deleteDir() = file().deleteDir()

/**
 * 删除文件夹
 * */
fun File?.deleteDir(): Boolean {
    if (this == null) return false
    if (!exists()) return true
    if (!isDirectory) return false
    listFiles()?.forEach {
        if (it.isFile) {
            if (!it.delete()) return false
        } else if (it.isDirectory) {
            if (!it.deleteDir()) return false
        }
    }
    return delete()
}

/**
 * 删除文件
 * */
fun String.deleteFile() = file().delete()

/**
 * 删除文件
 * */
fun File?.deleteFile(): Boolean {
    if (this == null) return false
    if (!exists()) return true
    if (isDirectory) {
        return deleteDir()
    } else if (isFile) {
        return delete()
    }
    return false
}

fun String.fileSize() = file().fileSize()

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
        val mimeType = file.typeFindSuffix()
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