package com.gjn.easyapp

import com.gjn.easyapp.network.MainRepository

class A3Repository {

    suspend fun getGirls(page: Int = 1) = MainRepository.getGirls(page)

    suspend fun getGirlsFlow(page: Int = 1) = MainRepository.getGirlsFlow(page)

    suspend fun getGirls2(page: Int = 1) = MainRepository.getGirls2(page)

}