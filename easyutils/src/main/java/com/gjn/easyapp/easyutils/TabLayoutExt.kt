package com.gjn.easyapp.easyutils

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.graphics.drawable.DrawableWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabScrollBar {

    private val tabLayout: TabLayout
    private val viewPager2: ViewPager2?
    private val fragment: Fragment?
    private val fragmentManager: FragmentManager
    private var customViewLayoutId: Int = View.NO_ID
    private var onCustomViewConvertCallback: OnCustomViewConvertCallback? = null

    val bars: MutableList<BarTab>
    var activity: FragmentActivity? = null
        private set
    var curPosition: Int = 0
        private set

    constructor(
        activity: FragmentActivity,
        tabLayout: TabLayout,
        viewPager2: ViewPager2?,
        bars: MutableList<BarTab> = mutableListOf()
    ) {
        this.activity = activity
        this.fragment = null
        fragmentManager = activity.supportFragmentManager

        this.tabLayout = tabLayout
        this.viewPager2 = viewPager2
        this.bars = bars
    }

    constructor(
        fragment: Fragment,
        tabLayout: TabLayout,
        viewPager2: ViewPager2?,
        bars: MutableList<BarTab> = mutableListOf()
    ) {
        this.activity = fragment.activity
        this.fragment = fragment
        this.fragmentManager = fragment.childFragmentManager

        this.tabLayout = tabLayout
        this.viewPager2 = viewPager2
        this.bars = bars
    }

    fun create() {
        if (bars.isEmpty()) {
            logW("bars is null.", TAG)
            return
        }
        if (viewPager2 == null) {
            //只做tab数据绑定
            for (bar in bars) {
                tabLayout.addTab(tabLayout.newTab().setText(bar.title))
            }
        } else {
            if (fragment != null) {
                viewPager2.adapter = ViewPager2Adapter(fragment)
            } else if (activity != null) {
                viewPager2.adapter = ViewPager2Adapter(activity!!)
            }
            val mediator = TabLayoutMediator(tabLayout, viewPager2) { _, _ -> }
            //绑定tabLayout viewPager2
            mediator.attach()
        }

        //自定义TabLayout变换view
        if (customViewLayoutId != View.NO_ID) {
            for (i in 0 until bars.size) {
                tabLayout.getTabAt(i)?.customView = createCustomView(i)
            }
        }

        //监听TabLayout切换
        addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.run {
                    curPosition = position
                    onCustomViewConvertCallback?.onTabSelected(customView)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.run {
                    onCustomViewConvertCallback?.onTabUnselected(customView)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun createCustomView(i: Int): View? {
        val view = activity.inflate(customViewLayoutId)
        onCustomViewConvertCallback?.convertData(view, bars[i], i, 0)
        return view
    }

    fun changeTitle(position: Int, title: String) {
        if (position < 0 || position >= bars.size) {
            logW("change Fail error $position not in 0-${bars.size}", TAG)
            return
        }
        if (customViewLayoutId == View.NO_ID) {
            tabLayout.getTabAt(position)?.text = title
        } else {
            bars[position].title = title
            val view = tabLayout.getTabAt(position)?.customView
            onCustomViewConvertCallback?.convertData(view, bars[position], position, curPosition)
        }
    }

    fun setCustomTabView(id: Int, callback: OnCustomViewConvertCallback?): TabScrollBar {
        customViewLayoutId = id
        onCustomViewConvertCallback = callback
        return this
    }

    fun addOnTabSelectedListener(listener: TabLayout.OnTabSelectedListener): TabScrollBar {
        tabLayout.addOnTabSelectedListener(listener)
        return this
    }

    fun setTabMode(@TabLayout.Mode mode: Int): TabScrollBar {
        tabLayout.tabMode = mode
        return this
    }

    fun setSelectedTabIndicator(drawable: Drawable?): TabScrollBar {
        tabLayout.setSelectedTabIndicator(drawable)
        return this
    }

    inner class ViewPager2Adapter : FragmentStateAdapter {

        constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)

        constructor(fragment: Fragment) : super(fragment)

        override fun getItemCount(): Int = bars.size

        override fun createFragment(position: Int): Fragment = bars[position].fragment!!
    }

    companion object {
        private const val TAG = "TabScrollBar"
    }
}

data class BarTab(
    var title: String,
    val fragment: Fragment? = null,
    var any: Any? = null
)

abstract class SimpleCustomViewConvertCallback : OnCustomViewConvertCallback {
    override fun onTabSelected(view: View?) {
    }

    override fun onTabUnselected(view: View?) {
    }
}

interface OnCustomViewConvertCallback {
    fun convertData(view: View?, barTab: BarTab, position: Int, select: Int)

    fun onTabSelected(view: View?)

    fun onTabUnselected(view: View?)
}

/**
 * 强制固定TabLayout宽度 兼容5.0 5.1
 * 地址 https://juejin.cn/post/6925318518128771079
 * */
fun TabLayout.setSelectedTabIndicatorFixWidth(width: Float) {
    setSelectedTabIndicator(object : DrawableWrapper(tabSelectedIndicator) {
        override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
            var realLeft = left
            var realRight = right
            if ((right - left).toFloat() != width) {
                val center = left + (right - left).toFloat() / 2
                realLeft = (center - width / 2).toInt()
                realRight = (center + width / 2).toInt()
            }
            super.setBounds(realLeft, top, realRight, bottom)
        }
    })
}