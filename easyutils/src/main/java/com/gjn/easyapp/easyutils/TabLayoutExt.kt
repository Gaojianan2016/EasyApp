package com.gjn.easyapp.easyutils

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.appcompat.graphics.drawable.DrawableWrapper
import androidx.databinding.ViewDataBinding
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
        bars: MutableList<BarTab> = mutableListOf(),
        viewPager2: ViewPager2? = null
    ) {
        this.activity = activity
        this.fragment = null
        fragmentManager = activity.supportFragmentManager

        this.tabLayout = tabLayout
        this.bars = bars
        this.viewPager2 = viewPager2
    }

    constructor(
        fragment: Fragment,
        tabLayout: TabLayout,
        bars: MutableList<BarTab> = mutableListOf(),
        viewPager2: ViewPager2? = null
    ) {
        this.activity = fragment.activity
        this.fragment = fragment
        this.fragmentManager = fragment.childFragmentManager

        this.tabLayout = tabLayout
        this.bars = bars
        this.viewPager2 = viewPager2
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
        if (i == 0) {
            onCustomViewConvertCallback?.onTabSelected(view)
        } else {
            onCustomViewConvertCallback?.onTabUnselected(view)
        }
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
            onCustomViewConvertCallback?.convertData(tabLayout.getTabAt(position)?.customView, bars[position], position, curPosition)
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

class TabScrollBar2<VDB : ViewDataBinding> {

    private val viewPager2: ViewPager2?
    private val fragment: Fragment?
    private val fragmentManager: FragmentManager
    private var customViewLayoutId: Int = View.NO_ID
    private val customViewDataBindingList = mutableListOf<VDB>()
    private var onCustomViewDataBindingConvertCallback: OnCustomViewDataBindingConvertCallback<VDB>? = null

    val bars: MutableList<BarTab>
    var activity: FragmentActivity?
        private set
    var tabLayout: TabLayout?
        private set
    var curPosition: Int = 0
        private set

    constructor(
        activity: FragmentActivity,
        tabLayout: TabLayout? = null,
        bars: MutableList<BarTab> = mutableListOf(),
        viewPager2: ViewPager2? = null
    ) {
        this.activity = activity
        this.fragment = null
        this.fragmentManager = activity.supportFragmentManager

        this.tabLayout = tabLayout
        this.bars = bars
        this.viewPager2 = viewPager2
    }

    constructor(
        fragment: Fragment,
        tabLayout: TabLayout? = null,
        bars: MutableList<BarTab> = mutableListOf(),
        viewPager2: ViewPager2? = null
    ) {
        this.activity = fragment.activity
        this.fragment = fragment
        this.fragmentManager = fragment.childFragmentManager

        this.tabLayout = tabLayout
        this.bars = bars
        this.viewPager2 = viewPager2
    }

    fun create(layout: TabLayout? = null, list: List<BarTab>? = null) {
        if (layout != null) tabLayout = layout
        if (list != null) bars.setAll(list)
        if (bars.isEmpty()) {
            logW("bars is null.", TAG)
            return
        }
        tabLayout?.run {
            removeAllTabs()
            customViewDataBindingList.clear()
            clearOnTabSelectedListeners()
            if (viewPager2 == null) {
                //只做tab数据绑定
                for (bar in bars) {
                    addTab(newTab().setText(bar.title))
                }
            } else {
                if (fragment != null) {
                    viewPager2.adapter = ViewPager2Adapter(fragment)
                } else if (activity != null) {
                    viewPager2.adapter = ViewPager2Adapter(activity!!)
                }
                val mediator = TabLayoutMediator(this, viewPager2) { _, _ -> }
                //绑定tabLayout viewPager2
                mediator.attach()
            }

            //自定义TabLayout变换view
            if (customViewLayoutId != View.NO_ID) {
                for (i in 0 until bars.size) {
                    getTabAt(i)?.customView = createCustomView(i)
                }
            }

            //监听TabLayout切换
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.run {
                        curPosition = position
                        onCustomViewDataBindingConvertCallback?.onTabSelected(customViewDataBindingList[position])
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.run {
                        onCustomViewDataBindingConvertCallback?.onTabUnselected(customViewDataBindingList[position])
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }

    private fun createCustomView(position: Int): View {
        val vdb = activity.inflateDataBindingUtil<VDB>(customViewLayoutId)
        customViewDataBindingList.add(position, vdb)
        onCustomViewDataBindingConvertCallback?.convertData(vdb, bars[position], position, 0)
        if (position == 0) {
            onCustomViewDataBindingConvertCallback?.onTabSelected(vdb)
        } else {
            onCustomViewDataBindingConvertCallback?.onTabUnselected(vdb)
        }
        return vdb.root
    }

    fun changeTitle(position: Int, title: String) {
        if (position < 0 || position >= bars.size) {
            logW("change Fail error $position not in 0-${bars.size}", TAG)
            return
        }
        tabLayout?.run {
            if (customViewLayoutId == View.NO_ID) {
                getTabAt(position)?.text = title
            } else {
                bars[position].title = title
                onCustomViewDataBindingConvertCallback?.convertData(customViewDataBindingList[position], bars[position], position, curPosition)
            }
        }
    }

    fun setCustomTabView(id: Int, callback: OnCustomViewDataBindingConvertCallback<VDB>?): TabScrollBar2<VDB> {
        customViewLayoutId = id
        onCustomViewDataBindingConvertCallback = callback
        return this
    }

    fun addOnTabSelectedListener(listener: TabLayout.OnTabSelectedListener): TabScrollBar2<VDB> {
        tabLayout?.addOnTabSelectedListener(listener)
        return this
    }

    inner class ViewPager2Adapter : FragmentStateAdapter {

        constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)

        constructor(fragment: Fragment) : super(fragment)

        override fun getItemCount(): Int = bars.size

        override fun createFragment(position: Int): Fragment = bars[position].fragment!!
    }

    companion object {
        private const val TAG = "TabScrollBar2"
    }
}

data class BarTab(
    var title: String,
    val fragment: Fragment? = null,
    var any: Any? = null
)

interface OnCustomViewConvertCallback {
    fun convertData(view: View?, barTab: BarTab, position: Int, select: Int)

    fun onTabSelected(view: View?)

    fun onTabUnselected(view: View?)
}

interface OnCustomViewDataBindingConvertCallback<VDB : ViewDataBinding> {
    fun convertData(databinding: VDB?, barTab: BarTab, position: Int, select: Int)

    fun onTabSelected(databinding: VDB?)

    fun onTabUnselected(databinding: VDB?)
}

/**
 * 隐藏TabLayout 自带长按提示toast
 * */
fun TabLayout.hideToolTipText() {
    for (i in 0 until tabCount) {
        getTabAt(i)?.let { tab ->
            tab.view.isLongClickable = false
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                tab.view.tooltipText = ""
            }
        }
    }
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