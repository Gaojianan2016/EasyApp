package com.gjn.easyapp

import android.content.Context
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.gjn.easyapp.easybase.BaseLazyFragment
import com.gjn.easyapp.easyutils.click
import com.gjn.easyapp.easyutils.createAndroidViewModel
import com.gjn.universaladapterlibrary.BaseRecyclerAdapter
import com.gjn.universaladapterlibrary.RecyclerViewHolder
import kotlinx.android.synthetic.main.fragment_a3.*

class A3Fragment : BaseLazyFragment() {

    private val vm by lazy {
        A3ViewModel::class.java.createAndroidViewModel(this, mActivity.application)
    }

    private lateinit var adapter: GirlAdapter

    override fun layoutId(): Int = R.layout.fragment_a3

    override fun initView() {
        println("A3Fragment initView")

        adapter = GirlAdapter(mActivity)
        rv_list.run {
            layoutManager = GridLayoutManager(mActivity, 2)
            adapter = this@A3Fragment.adapter
        }

        btn_load.click {
            vm.loadData()
        }

        vm.girlData.observe(this, Observer {
            adapter.data = it.data
        })
    }

    override fun lazyData() {
        println("A3Fragment lazyData")
    }

    override fun againUpdateData() {
        println("A3Fragment againUpdateData")
    }

    class GirlAdapter(context: Context): BaseRecyclerAdapter<Data>(context, R.layout.adapter_girl, null){

        override fun bindData(holder: RecyclerViewHolder?, item: Data?, position: Int) {
            holder?.getImageView(R.id.iv_ag)?.let { Glide.with(mActivity).load(item?.url).into(it) }
        }
    }
}