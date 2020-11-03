package com.gjn.easyapp

import com.gjn.easyapp.base.BaseVmFragment
import com.gjn.easyapp.databinding.FragmentA2Binding
import com.gjn.easyapp.easyutils.click
import com.gjn.easyapp.easyutils.createAndroidViewModel

class A2Fragment : BaseVmFragment<FragmentA2Binding>() {

    private val vm by lazy {
        A2ViewModel::class.java.createAndroidViewModel(this, mActivity.application)
    }

    override fun layoutId(): Int = R.layout.fragment_a2

    override fun initView() {
        println("A2Fragment initView")
        dataBinding.btn1.click {
            vm.updateData()
        }
    }

    override fun lazyData() {
        println("A2Fragment lazyData")
//        //ViewModel默认绑定方式
//        vm.data.observe(this, Observer {
//            dataBinding.tvCs.text = it
//        })
        //绑定DataBinding
        dataBinding.vm = vm
    }

}