package com.gjn.easyapp

class A3Repository {

    suspend fun getGirls(page: Int = 1) = MainRepository.getGirls(page)

}