package com.gjn.easyapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.gjn.easyapp.databinding.ActivityMainBinding
import com.gjn.easyapp.easybase.BaseDatabindingActivity
import com.gjn.easyapp.easybase.BaseLog
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseDatabindingActivity<ActivityMainBinding>() {

//    val vm by bindViewModel(A2ViewModel::class.java)

    val tabs = arrayOf(A1Fragment(), A2Fragment(), A3Fragment())
//    val tabTitles = arrayOf("A1Fragment", "A2Fragment", "A3Fragment")

    override fun layoutId(): Int = R.layout.activity_main

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        BaseLog.isDebug = true
    }

    override fun initView() {

        dataBinding.vpAm.run {
            //禁止预加载
            offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT

            adapter = object : FragmentStateAdapter(mActivity) {
                override fun createFragment(position: Int): Fragment = tabs[position]

                override fun getItemCount(): Int = tabs.size
            }
        }

        vp_am.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                println("onPageSelected $position")
            }
        })

        val mediator = TabLayoutMediator(tl_am, vp_am) { tab, position ->
            tab.text = "TAB $position"
        }

        //绑定TabLayout ViewPager2
        mediator.attach()
    }

    override fun initData() {
//        vm.updateBanner()
    }
}