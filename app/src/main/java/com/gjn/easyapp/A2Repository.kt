package com.gjn.easyapp

import com.gjn.easyapp.network.MainRepository

class A2Repository {

    suspend fun getGirls(page: Int = 1) = MainRepository.getGirls2(page)

    suspend fun getBanner() = MainRepository.getBanner()

}