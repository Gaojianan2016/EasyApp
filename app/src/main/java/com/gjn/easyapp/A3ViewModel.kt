package com.gjn.easyapp

import androidx.lifecycle.*
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class A3ViewModel : ViewModel() {

    private val a3Repository by lazy { A3Repository() }

//    //基础的ViewModel监听绑定
//    //私有的 可变
//    private val _gankData = MutableLiveData<GankResultData<List<GirlBean>>>()
//
//    //公有的 只可读
//    val gankData: LiveData<GankResultData<List<GirlBean>>> = _gankData
//
//    fun getGirl(){
//        viewModelScope.launch {
//            _gankData.value = a3Repository.getGirls()
//        }
//    }

//    //LiveDataScope 简化
//    private val girlPage = MutableLiveData<Int>()
//
//    val gankData2 = girlPage.switchMap {
//        liveData { emit(a3Repository.getGirls(it)) }
//    }
//
//    fun getGirl2(){
//        girlPage.value = 1
//    }

    //加入flow
    private val girlPage = MutableLiveData<Int>()

    val gankData3 = girlPage.switchMap {
        liveData {
            a3Repository.getGirlsFlow(it)
                .loading()
                .collectLatest { emit(it) }
        }
    }

    fun getGirlsFlow() {
        girlPage.value = 1
    }

    //paging 3
    fun getGirlsPagingData() =
        a3Repository.getGirlsPagingData().cachedIn(viewModelScope).asLiveData()

    fun <T> Flow<T>.loading() =
        this.onStart {
            //开始请求
            println("开始请求")
        }.catch { tr ->
            //报错
            println(tr.message)
        }.onCompletion {
            //完成
            println("完成请求")
        }

}