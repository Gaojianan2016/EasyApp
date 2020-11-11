package com.gjn.easyapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjn.easyapp.model.WanBannerBean
import com.gjn.easyapp.model.WanResultData
import kotlinx.coroutines.launch

class A2ViewModel : ViewModel(){

    var data = MutableLiveData<String>()

    var data2 = MutableLiveData<GirlBean>()

    var banner = MutableLiveData<List<WanBannerBean>>()

    private val a2Repository by lazy { A2Repository() }

    fun getGirls(){
        viewModelScope.launch {
            if (data2.value == null) {
                data2.postValue(a2Repository.getGirls())
            }else{
                data2.postValue(data2.value)
            }
        }
    }

    fun getBanner(){
        viewModelScope.launch {

            try {
                banner.value = a2Repository.getBanner()
            }catch (e: Exception){

            }
        }
    }

    fun updateData(){
        data.postValue("1-100随机数 ${(1..100).random()}")
    }
}