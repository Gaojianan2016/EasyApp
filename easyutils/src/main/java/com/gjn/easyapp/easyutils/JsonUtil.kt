package com.gjn.easyapp.easyutils

object JsonUtil {

    @JvmOverloads
    fun formatJson(json: String?, tab: String = ""): String {
        val sb = StringBuilder(tab)
        json?.run {
            val clearJson = replace("\\s*|\t|\r|\n".toRegex(), "")
            var current: Char
            val ln = '\n'
            var line = 0
            for (i in 0 until clearJson.length) {
                current = clearJson[i]
                when (current) {
                    //换行 并且加一级tab
                    '{', '[' -> {
                        sb.append(current).append(ln).append(tab)
                        line++
                        addTab(sb, line)
                    }
                    //换行 并且减一级tab
                    '}', ']' -> {
                        sb.append(ln).append(tab)
                        line--
                        addTab(sb, line)
                        sb.append(current)
                    }
                    //换行 并且同级tab
                    ',' -> {
                        sb.append(current).append(ln).append(tab)
                        addTab(sb, line)
                    }
                    else -> sb.append(current)
                }
            }
        }
        return sb.toString()
    }

    private fun addTab(sb: StringBuilder, line: Int) {
        for (i in 0 until line) {
            sb.append('\t')
        }
    }
}