package com.gjn.easyapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gjn.easyapp.easynetworker.RetrofitManager
import kotlinx.coroutines.launch
import okhttp3.Request

class A3ViewModel(application: Application) : AndroidViewModel(application) {

    var girlData = MutableLiveData<GirlBean>()

    fun loadData() {
        RetrofitManager.baseUrl = GankUrl.API_BASE
        RetrofitManager.customInterceptorListener =
            object : RetrofitManager.OnSimpleInterceptorListener() {
                override fun customRequest(url: String, builder: Request.Builder) {
                    builder.header("111", "222")
                }
            }
        val api = RetrofitManager.create(GankUrl::class.java)

        viewModelScope.launch {
//            val result = async { api.girls(1, 10).execute() }.await()

//            val result2 = async { api.girls2(1, 10) }.await()
//            girlData.postValue(result2)

            girlData.postValue(api.girls2(1, 10))
        }
    }

}