package com.gjn.easyapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData

class A3ViewModel(application: Application) : AndroidViewModel(application) {

    private val a3Repository by lazy { A3Repository() }

    val page = MutableLiveData<Int>()

    val data = Transformations.switchMap(page) {
        liveData {
            val result = a3Repository.getGirls(it)
            emit(result)
        }
    }

    fun loadData() {
        page.value = 1
    }

    fun nextData(){
        if (page.value == null) {
            page.value = 1
        }else{
            page.value = page.value!! + 1
        }
    }
}