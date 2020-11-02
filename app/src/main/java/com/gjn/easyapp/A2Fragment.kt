package com.gjn.easyapp

import androidx.lifecycle.ViewModelProvider
import com.gjn.easyapp.base.BaseVmFragment
import com.gjn.easyapp.databinding.FragmentA2Binding
import com.gjn.easyapp.easyutils.click

class A2Fragment : BaseVmFragment<FragmentA2Binding>() {


    lateinit var vm: A2ViewModel

    override fun layoutId(): Int = R.layout.fragment_a2

    override fun initView() {
        println("A2Fragment initView")
        dataBinding.btn1.click {
            vm.changeUrl()
            println("修改后 vm.url=${vm.getUrl().value}")
        }
    }

    override fun lazyData() {
        println("A2Fragment lazyData")

        vm = ViewModelProvider.NewInstanceFactory().create(A2ViewModel::class.java)
        dataBinding.vm = vm
        println("默认 vm.url=${vm.getUrl().value}")
    }

}