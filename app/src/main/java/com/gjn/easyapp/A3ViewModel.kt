package com.gjn.easyapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData

class A3ViewModel(application: Application) : AndroidViewModel(application) {

    private val a3Repository by lazy { A3Repository() }

    private val page = MutableLiveData<Int>()

    val data = Transformations.switchMap(page) {
        liveData {
            val result = kotlin.runCatching {
//                a3Repository.getGirls(it)
                a3Repository.getGirls2(it)
            }
            emit(result)
        }
    }

    fun onRefresh() {
        page.value = 1
    }

    fun onLoadMore() {
        if (page.value == null) {
            page.value = 1
        } else {
            page.value = page.value!! + 1
        }
    }
}