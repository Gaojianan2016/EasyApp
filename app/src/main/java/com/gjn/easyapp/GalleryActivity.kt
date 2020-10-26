package com.gjn.easyapp

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.MediaInfo
import com.gjn.easyapp.easyutils.MediaStorageManager
import com.gjn.universaladapterlibrary.BaseRecyclerAdapter
import com.gjn.universaladapterlibrary.RecyclerViewHolder
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : ABaseActivity() {

    val mediaStorageManager by lazy { MediaStorageManager(mContext) }

    val imgAdapter by lazy { ImgAdapter(mActivity) }

    override fun layoutId(): Int = R.layout.activity_gallery

    override fun initView() {

        rv_test.run {
            layoutManager = GridLayoutManager(mActivity, 4)
            adapter = imgAdapter
        }
    }

    override fun initData() {
        mediaStorageManager.run {
            onMediaNotifyListener =
                object : MediaStorageManager.OnMediaNotifyListener {
                    override fun preLoad() {
                        println("开始扫描")
                    }

                    override fun updateList(cancelList: MutableList<MediaInfo>) {
                        for (mediaInfo in cancelList) {
                            imgAdapter.add(mediaInfo)
                        }
                    }

                    override fun complete(dirs: MutableList<MediaInfo>) {
                        println("数据加载完成,父文件 ${dirs.size}")
                    }
                }
            startScanMedia()
        }
    }

    class ImgAdapter(context: Context): BaseRecyclerAdapter<MediaInfo>(context, R.layout.adapter_img_list, null){

        override fun bindData(holder: RecyclerViewHolder?, item: MediaInfo?, position: Int) {

            println("----------> ${item?.path}")

            Glide.with(mActivity).load(item!!.path).into(holder!!.getImageView(R.id.iv_ail))
        }
    }

}