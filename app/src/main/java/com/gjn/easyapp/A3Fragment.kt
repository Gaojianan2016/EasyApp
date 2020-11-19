package com.gjn.easyapp

import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.gjn.easyapp.base.BaseKtRecyclerAdapter
import com.gjn.easyapp.base.BaseVH
import com.gjn.easyapp.easybase.BaseLazyFragment
import com.gjn.easyapp.easyutils.click
import com.gjn.easyapp.easyutils.createAndroidViewModel
import kotlinx.android.synthetic.main.adapter_girl.view.*
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

        btn_next.click {
            vm.nextData()
        }

        adapter.onItemClickListener = object : BaseKtRecyclerAdapter.OnItemClickListener<GirlBean> {
            override fun onClick(view: View, item: GirlBean, position: Int) {
                showToast("点击 ${item.url}")
            }
        }

        vm.page.observe(this, Observer {
            if (it == 1) {
                adapter.clear()
            }
        })

        vm.data.observe(this, Observer {
            adapter.add(it.toMutableList())
        })
    }

    override fun lazyData() {
        println("A3Fragment lazyData")
    }

    override fun againUpdateData() {
        println("A3Fragment againUpdateData")
    }


    class GirlAdapter(context: Context) :
        BaseKtRecyclerAdapter<GirlBean>(context, R.layout.adapter_girl) {

        override fun convertData(holder: BaseVH, item: GirlBean, position: Int) {
            Glide.with(context).load(item.url).into(holder.itemView.iv_ag)
        }
    }

}