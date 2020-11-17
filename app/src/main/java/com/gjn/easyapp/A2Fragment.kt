package com.gjn.easyapp

import androidx.lifecycle.Observer
import com.gjn.easyapp.databinding.FragmentA2Binding
import com.gjn.easyapp.easybase.BaseDatabindingFragment
import com.gjn.easyapp.easyutils.createAndroidViewModel

class A2Fragment : BaseDatabindingFragment<FragmentA2Binding>() {

    private val vm by lazy {
        A2ViewModel::class.java.createAndroidViewModel(this, mActivity.application)
    }

    override fun layoutId(): Int = R.layout.fragment_a2

    override fun initView() {
        println("A2Fragment initView")

        dataBinding.click = ClickListener()
//        dataBinding.btn1.click {
//            vm.updateData()
//        }
    }

    override fun lazyData() {
        println("A2Fragment lazyData")

        vm.girls.observe(this, Observer {
            DataBindingHelper.drawImage(dataBinding.ivImg1, it[0].url)
        })

        vm.banners.observe(this, Observer {
            DataBindingHelper.drawImage(dataBinding.ivImg2, it[0].imagePath)
        })
    }

    open inner class ClickListener{

        fun btn1(){
            showToast("点击btn1按钮")
            vm.updateGirls()
        }

        fun btn2(){
            showToast("点击btn2按钮")
            vm.updateBanner()
        }

        fun btn3(){
            showToast("点击btn3按钮")
            vm.mergeData()
        }
    }
}