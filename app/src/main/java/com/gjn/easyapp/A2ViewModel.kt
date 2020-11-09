package com.gjn.easyapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class A2ViewModel : ViewModel(){

    var data = MutableLiveData<String>()

    var data2 = MutableLiveData<GirlBean>()

    fun getGirls(){
        viewModelScope.launch {
            if (data2.value == null) {
                data2.postValue(A2Repository.getGirls())
            }else{
                data2.postValue(data2.value)
            }
        }
    }

    fun updateData(){
        data.postValue("1-100随机数 ${(1..100).random()}")
    }
}