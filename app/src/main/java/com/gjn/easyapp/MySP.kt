package com.gjn.easyapp

import android.content.Context
import com.gjn.easyapp.easyutils.SharedPref

class MySP<T>(context: Context = App.INSTANCE) : SharedPref<T>(context) {

    constructor(
        key: String,
        defValue: T,
        name: String = "${App.INSTANCE.packageName}_sp",
        commit: Boolean = false
    ) : super(App.INSTANCE, key, defValue, name, commit)

}