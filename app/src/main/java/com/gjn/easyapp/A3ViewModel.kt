package com.gjn.easyapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gjn.easyapp.easyutils.launchIO
import retrofit2.Retrofit

class A3ViewModel : ViewModel(){

    var girlData = MutableLiveData<GirlBean>()

    fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(Url.API_BASE)
            .build()
        val api = retrofit.create(Url::class.java)

        launchIO {
            val result = api.girls(1, 10)
            println("result = ${result.string()}")
        }
    }

}