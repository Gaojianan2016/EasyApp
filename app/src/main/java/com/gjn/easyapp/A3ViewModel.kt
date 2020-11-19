package com.gjn.easyapp

import android.app.Application
import androidx.lifecycle.*
import com.gjn.easyapp.model.GankResultData
import com.gjn.easyapp.network.AppNetWorker
import com.gjn.easyapp.network.RequestStatus
import com.gjn.easyapp.network.ResultData
import com.gjn.easyapp.network.requestLiveData

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


    //dsl 方法调用网络
    val dslData = MutableLiveData<ResultData<GankResultData<List<GirlBean>>>>()

    fun  dslNet(){
        val liveData =
            viewModelScope.requestLiveData<GankResultData<List<GirlBean>>> {
                api { AppNetWorker.getInstant().gankApi.girls(1, 10) }
            }

        if(liveData.value?.requestStatus == RequestStatus.COMPLETE){
            dslData.value = liveData.value
        }

    }
}