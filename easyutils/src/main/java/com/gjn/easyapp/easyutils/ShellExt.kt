package com.gjn.easyapp.easyutils

import java.io.BufferedReader
import java.io.Closeable
import java.io.DataOutputStream
import java.io.InputStreamReader

private val LINE_SEP = System.getProperty("line.separator")

/**
 * 执行命令
 * */
fun execCmd(
    command: String,
    isRooted: Boolean,
    isNeedResultMsg: Boolean = false
) = execCmd(arrayOf(command), isRooted, isNeedResultMsg)

/**
 * 执行命令
 * */
fun execCmd(
    commands: Array<String?>,
    isRooted: Boolean,
    isNeedResultMsg: Boolean = true
): CommandResult {
    if (commands.isEmpty()) return CommandResult(-1)

    var result = -1
    var process: Process? = null
    var os: DataOutputStream? = null

    var successMsg: StringBuilder = StringBuilder()
    var errorMsg: StringBuilder = StringBuilder()
    var successResult: BufferedReader? = null
    var errorResult: BufferedReader? = null

    try {
        process = Runtime.getRuntime().exec(if (isRooted) "su" else "sh")
        os = DataOutputStream(process.outputStream)

        for (command in commands) {
            if (command == null) continue
            os.write(command.toByteArray())
            os.writeBytes(LINE_SEP)
            os.flush()
        }

        os.writeBytes("exit$LINE_SEP")
        os.flush()
        result = process.waitFor()
        if (isNeedResultMsg) {
            successMsg = StringBuilder()
            errorMsg = StringBuilder()
            successResult = BufferedReader(InputStreamReader(process?.inputStream, "UTF-8"))
            errorResult = BufferedReader(InputStreamReader(process?.errorStream, "UTF-8"))
            var line: String?
            if (successResult.readLine().also { line = it } != null) {
                successMsg.append(line)
                while (successResult.readLine().also { line = it } != null) {
                    successMsg.append(LINE_SEP).append(line)
                }
            }
            if (errorResult.readLine().also { line = it } != null) {
                errorMsg.append(line)
                while (errorResult.readLine().also { line = it } != null) {
                    errorMsg.append(LINE_SEP).append(line)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        os?.tryClose()
        successResult?.tryClose()
        errorResult?.tryClose()
        try {
            process?.destroy()
        } catch (e: Exception) {
        }
    }
    return CommandResult(result, successMsg.toString(), errorMsg.toString())
}

/**
 * 尝试关闭流操作
 * */
fun Closeable.tryClose() {
    try {
        close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 命令结果
 * */
class CommandResult(val result: Int, val successMsg: String = "", val errorMsg: String = "") {

    override fun toString() = "result=$result, successMsg=$successMsg, errorMsg=$errorMsg"
}