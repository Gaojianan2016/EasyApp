package com.gjn.easyapp

import androidx.lifecycle.*
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

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

    @OptIn(ExperimentalCoroutinesApi::class)
    val gankData3 = girlPage.switchMap {
        liveData {
            a3Repository.getGirlsFlow(it)
                .onStart {
                    //开始请求
                    println("开始请求")
                }
                .catch {
                    //报错
                    println("请求出错")
                }
                .onCompletion {
                    //完成
                    println("完成请求")
                }
                .collectLatest {
                    emit(it)
                }
        }
    }

    //paging 3
    fun getGirlsPagingData() =
        a3Repository.getGirlsPagingData().cachedIn(viewModelScope).asLiveData()

    fun getGirlsFlow() {
        girlPage.value = 1
    }

}