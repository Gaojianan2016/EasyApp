package com.gjn.easyapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class A2ViewModel : ViewModel(){

    var data = MutableLiveData<String>()

    fun updateData(){
        data.postValue("1-100随机数 ${(1..100).random()}")
    }
}