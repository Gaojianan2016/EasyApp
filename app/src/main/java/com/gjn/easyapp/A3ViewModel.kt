package com.gjn.easyapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gjn.easyapp.easynetworker.RetrofitManager
import kotlinx.coroutines.launch
import okhttp3.Request
import okhttp3.Response

class A3ViewModel : ViewModel() {

    var girlData = MutableLiveData<GirlBean>()

    fun loadData() {

        RetrofitManager.baseUrl = GankUrl.API_BASE
        RetrofitManager.listener = object : RetrofitManager.OnCustomHeaderListener {
            override fun customRequest(url: String, builder: Request.Builder) {
                builder.header("111", "222")
            }

            override fun getResponse(response: Response) {
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