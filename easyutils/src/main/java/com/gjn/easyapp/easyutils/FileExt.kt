package com.gjn.easyapp.easyutils

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.StatFs
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import net.lingala.zip4j.ZipFile
import java.io.*
import java.util.*
import java.util.zip.ZipInputStream

private const val FILEPROVIDER = ".fileprovider"
private const val APP = "/app"
private const val DATA = "_data"

inline val String.file: File get() = File(this)

inline val Uri.file: File get() = File(path)

inline val fileSeparator: String get() = File.separator

inline val filePathSeparator: String get() = File.pathSeparator

/**
 * 文件名
 * */
inline val File.fileName: String
    get() {
        val lastSep = absolutePath.lastIndexOf(fileSeparator)
        return if (lastSep == -1) absolutePath else absolutePath.substring(lastSep + 1)
    }

/**
 * 文件长度/大小
 * */
inline val File.fileLength: Long
    get() = if (isDirectory) {
        var len = 0L
        listFiles()?.forEach { len += if (it.isDirectory) it.fileLength else it.length() }
        len
    } else {
        length()
    }

/**
 * 文件后缀
 * */
inline val File.suffix: String get() = name.run { return substring(lastIndexOf('.') + 1) }

/**
 * mimeType 获取 文件后缀
 * i.e. text/plain -> txt
 * */
inline val File.extension: String?
    get() = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)

/**
 * 文件后缀 获取 mimeType
 * i.e. txt -> text/plain
 * */
inline val File.mimeType: String?
    get() = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix)

/**
 * file转换为byte[]
 * */
inline val File.byteArray: ByteArray get() = inputStream().use { return it.readBytes() }

/**
 * 是否是可用文件夹
 * */
inline val File.isAvailableDir: Boolean get() = exists() && isDirectory

/**
 * 是否是可用文件
 * */
inline val File.isAvailableFile: Boolean get() = exists() && isFile

/**
 * 获取StatFs总大小
 * */
inline val File.statFsTotalSize: Long
    get() = if (path.isEmpty()) 0L else StatFs(path).run { blockSizeLong * blockCountLong }

/**
 * 获取StatFs可用大小
 * */
inline val File.statFsAvailableSize: Long
    get() = if (path.isEmpty()) 0L else StatFs(path).run { blockSizeLong * availableBlocksLong }

/**
 * 文件重命名
 * @param newName 新名字
 * */
fun File.rename(newName: String): Boolean {
    if (!exists()) return false
    if (newName.isEmpty() || name.isEmpty()) return false
    if (newName == name) return true
    val newFile = "$parent$fileSeparator$newName".file
    return !newFile.exists() && this.renameTo(newFile)
}

/**
 * 创建父目录
 * */
fun File.createParentDir(): Boolean = parentFile?.createOrExistsDir() ?: false

/**
 * 创建文件夹(存在不处理)
 * */
fun File.createOrExistsDir(): Boolean = if (exists()) isDirectory else mkdirs()

/**
 * 创建文件(存在不处理)
 * */
fun File.createOrExistsFile(): Boolean {
    if (exists()) return isFile
    try {
        return if (createParentDir()) true else createNewFile()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * 创建新文件夹(存在删除)
 * */
fun File.createDir(): Boolean {
    if (exists() && !delete()) return false
    return if (createOrExistsDir()) true else mkdirs()
}

/**
 * 创建新文件(存在删除)
 * */
fun File.createFile(): Boolean {
    if (exists() && !delete()) return false
    try {
        return if (createOrExistsFile()) true else createNewFile()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * 复制文件
 * @param targetPath 目标路径
 * */
fun File.copyToPath(targetPath: String): Boolean {
    if (targetPath.isEmpty()) return false
    return copyToPath(targetPath.file)
}

/**
 * 移动文件
 * @param targetPath 目标路径
 * */
fun File.moveToPath(targetPath: String): Boolean {
    if (targetPath.isEmpty()) return false
    return moveToPath(targetPath.file)
}

/**
 * 复制文件
 * @param target 目标文件
 * */
fun File.copyToPath(target: File): Boolean =
    if (isDirectory) copyOrMoveDir(target) else copyOrMoveFile(target)

/**
 * 移动文件
 * @param target 目标文件
 * */
fun File.moveToPath(target: File): Boolean =
    if (isDirectory) copyOrMoveDir(target, true) else copyOrMoveFile(target, true)

/**
 * 复制或者移动文件夹
 * @param target 目标文件
 * @param move   true 移动 false 复制
 * */
fun File.copyOrMoveDir(target: File, move: Boolean = false): Boolean {
    //目标文件夹判断
    if (target.path.contains(path)) return false

    //文件夹不可用
    if (!isAvailableDir) return false

    //创建文件夹失败
    if (!target.createOrExistsDir()) return false

    //遍历
    listFiles()?.forEach {
        val tempFile = (target.path + fileSeparator + it.name).file
        if (it.isFile) {
            if (!it.copyOrMoveFile(tempFile, move)) return false
        } else if (it.isDirectory) {
            if (!it.copyOrMoveDir(tempFile, move)) return false
        }
    }

    return !move || delete()
}

/**
 * 复制或者移动文件
 * @param target 目标文件
 * @param move   true 移动 false 复制
 * */
fun File.copyOrMoveFile(target: File, move: Boolean = false): Boolean {
    //目标文件判断
    if (this == target) return false

    //文件不可用
    if (!isAvailableFile) return false

    //文件存在 删除失败
    if (target.exists() && !target.delete()) return false

    //文件操作
    try {
        return target.writeInputStream(inputStream()) && !(move && !deleteFile())
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return false
}

/**
 * 删除文件夹
 * */
fun File.deleteDir(): Boolean {
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
fun File.deleteFile(): Boolean {
    if (!exists()) return true
    if (isDirectory) {
        return deleteDir()
    } else if (isFile) {
        return delete()
    }
    return false
}

/**
 * 查找文件
 * @param filter        文件过滤器
 * @param isRecursive   是否递归（遍历文件夹全部文件）
 * @param comparator    排序方式
 * */
fun File.findListFiles(
    filter: FileFilter = FileFilter { return@FileFilter true },
    isRecursive: Boolean = true,
    comparator: Comparator<File>? = null
): List<File> {
    if (!exists() || !isDirectory) return emptyList()

    //遍历
    val files = mutableListOf<File>()
    listFiles()?.forEach {
        if (filter.accept(it)) {
            files.add(it)
        }
        if (isRecursive && it.isDirectory) {
            files.addAll(it.findListFiles(filter))
        }
    }

    //排序
    if (comparator != null) {
        Collections.sort(files, comparator)
    }

    return files
}

/**
 * 更新媒体文件
 * */
@Deprecated("适用于Android 10 以下的版本")
fun File.notifyMediaFile(context: Context) {
    notifyScanMediaFile(context)
    notifyInsertThumbnail(context)
}

/**
 * 通知文件夹扫描媒体文件
 * */
@Deprecated("适用于Android 10 以下的版本")
fun File.notifyScanMediaFile(context: Context) {
    if (!exists()) return
    context.sendBroadcast(
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also {
            it.data = "file://${this.absolutePath}".uri()
        }
    )
}

/**
 * 通知文件夹插入缩略图
 * */
@Deprecated("适用于Android 10 以下的版本")
fun File.notifyInsertThumbnail(context: Context) {
    if (!exists()) return
    MediaStore.Images.Media.insertImage(context.contentResolver, absolutePath, name, null)
}

/**
 * 打开文件
 * */
fun Context.openFile(file: File) {
    if (!file.exists()) return
    file.mimeType.run {
        Intent(Intent.ACTION_VIEW).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            } else {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            it.setDataAndType(getLocalFileUri(file), this)
            this@openFile.startActivity(it)
        }
    }
}

/**
 * 获取本地文件uri
 * */
fun Context.getLocalFileUri(file: File): Uri =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        FileProvider.getUriForFile(this, packageName + FILEPROVIDER, file)
    else
        Uri.fromFile(file)

/**
 * 获取本地文件通过uri
 * */
fun Context.getLocalFileFromUri(uri: Uri): File? =
    when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> getLocalFileFromContentUri(uri)
        ContentResolver.SCHEME_FILE -> uri.file
        else -> null
    }

/**
 * 获取本地文件通过ContentUri
 * */
private fun Context.getLocalFileFromContentUri(uri: Uri): File? {
    var filePath = ""
    try {
        if (uri.authority!!.contains(packageName)) {
            filePath = uri.path!!.replace(APP, "")
        } else {
            contentResolver.query(uri, arrayOf(DATA), null, null, null)?.run {
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
    return if (filePath.isEmpty()) null else filePath.file
}

/**
 * 解压assets文件
 * */
fun Context.unzipAssetsFile(assetName: String, targetPath: String) =
    unzipAssetsFile(assetName, targetPath.file)

/**
 * 解压assets文件
 * @param assetName 文件名
 * @param target    目标文件
 * */
fun Context.unzipAssetsFile(assetName: String, target: File?): Boolean {
    if (assetName.isEmpty() || target == null) return false
    if (!target.createOrExistsDir()) return false
    try {
        ZipInputStream(assets.open(assetName)).use { input ->
            var zipEntry = input.nextEntry
            var file: File
            while (zipEntry != null) {
                file = File(target.path + fileSeparator + zipEntry.name)
                if (zipEntry.isDirectory) {
                    file.createDir()
                } else {
                    file.createFile()
                    input.copyTo(file.outputStream())
                }
                zipEntry = input.nextEntry
            }
        }
        return true
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * zip4j解压文件
 * github地址 https://github.com/srikanth-lingala/zip4j
 * */
fun File.unzipFileUseZip4j(outputDir: String, password: CharArray? = null) {
    if (!exists()) return
    val zipFile = ZipFile(absolutePath)
    if (zipFile.isEncrypted) {
        zipFile.setPassword(password)
    }
    zipFile.extractAll(outputDir)
}