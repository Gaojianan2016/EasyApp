package com.gjn.easyapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.paging.PagingSource
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.gjn.easyapp.base.BaseKtRecyclerAdapter
import com.gjn.easyapp.base.BaseVH
import com.gjn.easyapp.easybase.BaseLazyFragment
import com.gjn.easyapp.easyutils.ResourcesUtils
import com.gjn.easyapp.easyutils.click
import com.gjn.easyapp.network.AppNetWorker
import kotlinx.android.synthetic.main.adapter_girl.view.*
import kotlinx.android.synthetic.main.fragment_a3.*
import kotlinx.coroutines.flow.collectLatest

class A3Fragment : BaseLazyFragment() {

    private val vm by bindViewModel(A3ViewModel::class.java)

    private lateinit var adapter: GirlAdapter
    private lateinit var pagingDataAdapter: GirlPagingDataAdapter

    override fun layoutId(): Int = R.layout.fragment_a3

    override fun initView() {
        println("A3Fragment initView")

        adapter = GirlAdapter(mActivity)

        pagingDataAdapter = GirlPagingDataAdapter()

        rv_list.run {
            layoutManager = GridLayoutManager(mActivity, 2)
//            adapter = this@A3Fragment.adapter
            adapter = this@A3Fragment.pagingDataAdapter
        }


        lifecycleScope.launchWhenCreated {
            pagingDataAdapter.loadStateFlow.collectLatest {
                println("loadState $it")
            }
        }


        btn_load.click {
//            vm.getGirl()
//            vm.getGirl2()
//            vm.getGirlsFlow()
        }

        btn_next.click {

        }

        adapter.onItemClickListener = object : BaseKtRecyclerAdapter.OnItemClickListener<GirlBean> {
            override fun onClick(view: View, item: GirlBean, position: Int) {
                showToast("点击 ${item.url}")
            }
        }

    }

    override fun lazyData() {
        println("A3Fragment lazyData")
//        vm.gankData.observe(this, Observer { data->
//            if (data.page == 1) {
//                adapter.data = data.data.toMutableList()
//            }else{
//                adapter.add(data.data.toMutableList())
//            }
//        })

//        vm.gankData2.observe(this, Observer { data ->
//            if (data.page == 1) {
//                adapter.data = data.data.toMutableList()
//            }else{
//                adapter.add(data.data.toMutableList())
//            }
//        })

        vm.gankData3.observe(this, Observer { data ->
            if (data.page == 1) {
                adapter.data = data.data.toMutableList()
            }else{
                adapter.add(data.data.toMutableList())
            }
        })

        vm.getGirlsPagingData().observe(this, Observer { data ->
            pagingDataAdapter.submitData(lifecycle, data)
        })
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

    class GirlPagaDataSource: PagingSource<Int, GirlBean>(){

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GirlBean> {
            val pos = params.key ?: 0
            val startIndex = pos * params.loadSize + 1
            val endIndex = (pos + 1) * params.loadSize
            return try {
                val list = AppNetWorker.getInstant().gankApi.girls(pos, 10).data
                LoadResult.Page(
                    list,
                    if (pos <= 0) null else pos - 1,
                    if(list.isNullOrEmpty()) null else pos + 1
                )
            }catch (e: Exception){
                LoadResult.Error(e)
            }
        }
    }

    class GirlPagingDataAdapter: PagingDataAdapter<GirlBean, BaseVH>(
        object : DiffUtil.ItemCallback<GirlBean>(){
            override fun areItemsTheSame(oldItem: GirlBean, newItem: GirlBean): Boolean =
                oldItem._id == newItem._id

            override fun areContentsTheSame(oldItem: GirlBean, newItem: GirlBean): Boolean =
                oldItem == newItem
        }){

        override fun onBindViewHolder(holder: BaseVH, position: Int) {
            Glide.with(holder.itemView.context).load(getItem(position)?.url).into(holder.itemView.iv_ag)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
            return BaseVH(ResourcesUtils.inflate(parent.context, R.layout.adapter_girl, parent)!!)
        }

    }

}