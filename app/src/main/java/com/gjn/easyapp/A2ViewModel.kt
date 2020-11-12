package com.gjn.easyapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjn.easyapp.model.WanBannerBean
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class A2ViewModel : ViewModel(){

    val girls = MutableLiveData<List<GirlBean>>()

    val banners = MutableLiveData<List<WanBannerBean>>()

    private val a2Repository by lazy { A2Repository() }

    fun updateGirls(){
        viewModelScope.launch {
            girls.value = a2Repository.getGirls()
        }
    }

    fun updateBanner(){
        viewModelScope.launch {
            banners.value = a2Repository.getBanner()
        }
    }

    fun mergeData(){
        viewModelScope.launch {
            val data1 =async {
                a2Repository.getGirls()
            }.await()
            val data2 =async {
                a2Repository.getBanner()
            }.await()
            val result = data1 + data2
            println("大小 ${result.size}")
        }
    }
}