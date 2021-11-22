package com.gjn.easyapp.easyutils

/**
 * json 格式化
 * */
fun String?.formatJson(
    tab: String = "",
    cleanRegex: Regex = "[\t\r\n]".toRegex()
): String {
    if (isNullOrEmpty()) return ""
    val result = StringBuilder()
    val rawJson = replace(cleanRegex, "")
    val ln = '\n'
    var line = 0
    for (element in rawJson) {
        when (element) {
            //换行 并且加一级tab
            '{', '[' -> {
                result.append(element).append(ln).append(tab)
                line++
                result.addTab(line)
            }
            //换行 并且减一级tab
            '}', ']' -> {
                result.append(ln).append(tab)
                line--
                result.addTab(line)
                result.append(element)
            }
            //换行 并且同级tab
            ',' -> {
                result.append(element).append(ln).append(tab)
                result.addTab(line)
            }
            else -> result.append(element)
        }
    }
    return result.toString()
}

private fun StringBuilder.addTab(line: Int) {
    for (i in 0 until line) {
        append('\t')
    }
}