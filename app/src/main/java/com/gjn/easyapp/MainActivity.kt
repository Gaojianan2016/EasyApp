package com.gjn.easyapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easybase.BaseLog
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ABaseActivity() {

    val tabs = arrayOf(A1Fragment(), A2Fragment(), A3Fragment())
    val tabTitles = arrayOf("A1Fragment", "A2Fragment", "A3Fragment")

    override fun layoutId(): Int = R.layout.activity_main

    override fun init() {
        super.init()
        BaseLog.isDebug = true
    }

    override fun initView() {

        vp_am.run {
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

        val mediator = TabLayoutMediator(
            tl_am,
            vp_am,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                //自定义 TabView
                val tabView = TextView(mActivity)

                val states = arrayOfNulls<IntArray>(2)
                states[0] = intArrayOf(android.R.attr.state_selected)
                states[1] = intArrayOf()
                val colors = intArrayOf(Color.RED, Color.BLACK)
                val colorList = ColorStateList(states, colors)

                tabView.run {
                    text = tabTitles[position]
                    setTextColor(colorList)
                }

                tab.customView = tabView
            })

        //绑定TabLayout ViewPager2
        mediator.attach()
    }

    override fun initData() {

    }
}