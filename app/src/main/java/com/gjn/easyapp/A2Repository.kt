package com.gjn.easyapp

class A2Repository {

    suspend fun getGirls(page: Int = 1) = MainRepository.getGirls(page)

    suspend fun getBanner() = MainRepository.getBanner()

}