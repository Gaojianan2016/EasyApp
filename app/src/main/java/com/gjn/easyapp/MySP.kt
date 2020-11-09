package com.gjn.easyapp

import com.gjn.easyapp.easyutils.SharedPref

class MySP<T> : SharedPref<T> {

    constructor() : super(App.instance)

    @JvmOverloads
    constructor(
        key: String,
        defValue: T,
        name: String = "${App.instance.packageName}_sp",
        commit: Boolean = false
    ) : super(App.instance, key, defValue, name, commit)

}