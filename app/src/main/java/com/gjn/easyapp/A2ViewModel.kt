package com.gjn.easyapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class A2ViewModel : ViewModel(){

    private val url = MutableLiveData<String>()


    fun changeUrl(){
        url.value = "url 变化 "
    }

    fun getUrl(): MutableLiveData<String> = url
}