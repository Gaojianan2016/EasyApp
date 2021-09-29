package com.gjn.easyapp.easyutils

object JsonUtil {

    fun formatJson(
        json: String?,
        tab: String = "",
        cleanRegex: Regex = "\t|\r|\n".toRegex()
    ): String {
        val sb = StringBuilder(tab)
        json?.run {
            val rawJson = replace(cleanRegex, "")
            val ln = '\n'
            var line = 0
            for (element in rawJson) {
                when (element) {
                    //换行 并且加一级tab
                    '{', '[' -> {
                        sb.append(element).append(ln).append(tab)
                        line++
                        sb.addTab(line)
                    }
                    //换行 并且减一级tab
                    '}', ']' -> {
                        sb.append(ln).append(tab)
                        line--
                        sb.addTab(line)
                        sb.append(element)
                    }
                    //换行 并且同级tab
                    ',' -> {
                        sb.append(element).append(ln).append(tab)
                        sb.addTab(line)
                    }
                    else -> sb.append(element)
                }
            }
        }
        return sb.toString()
    }

    private fun StringBuilder.addTab(line: Int) {
        for (i in 0 until line) {
            append('\t')
        }
    }
}