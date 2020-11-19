package com.gjn.easyapp

import com.gjn.easyapp.network.MainRepository

class A3Repository {

    suspend fun getGirls(page: Int = 1) = MainRepository.getGirls(page)


}