package com.gjn.easyapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class A3ViewModel(application: Application) : AndroidViewModel(application) {

    var girlData = MutableLiveData<List<GirlBean>>()

    fun loadData() {
        viewModelScope.launch {

            girlData.postValue(AppNetWorker.getInstant().gankApi.girls(1, 10).result())
        }
    }

}