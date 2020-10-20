package com.gjn.easyapp

import com.gjn.easyapp.easyutils.SharedPref

class MySP<T> : SharedPref<T> {

    constructor() : super(App.INSTANCE)

    constructor(
        key: String,
        defValue: T,
        name: String = "${App.INSTANCE.packageName}_sp",
        commit: Boolean = false
    ) : super(App.INSTANCE, key, defValue, name, commit)

}