package com.gjn.easyapp

import com.gjn.easyapp.easyutils.SharedPref

class MySP<T> @JvmOverloads constructor(
    key: String,
    defValue: T,
    name: String = "${App.instance.packageName}_sp",
    commit: Boolean = false
) : SharedPref<T>(App.instance, key, defValue, name, commit)