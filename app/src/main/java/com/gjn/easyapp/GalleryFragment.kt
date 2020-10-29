package com.gjn.easyapp

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.gjn.easyapp.easybase.ABaseFragment
import com.gjn.easyapp.easyutils.media.MediaInfo
import com.gjn.easyapp.easyutils.media.MediaStorageManager
import com.gjn.universaladapterlibrary.BaseRecyclerAdapter
import com.gjn.universaladapterlibrary.RecyclerViewHolder
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : ABaseFragment() {

    private val mMediaStorageManager by lazy { MediaStorageManager(mActivity) }

    private val imgAdapter by lazy { ImgAdapter(mActivity) }

    override fun layoutId(): Int = R.layout.fragment_gallery

    override fun initView() {
        rv_fg.run {
            layoutManager = GridLayoutManager(mActivity, 4)
            adapter = imgAdapter
        }
    }

    override fun initData() {
        //获取数据
        mMediaStorageManager.run {
            scanCallback = object : MediaStorageManager.ScanCallback{
                override fun preStart() {
                    pb_fg.visibility = View.VISIBLE
                }

                override fun complete(
                    infoList: MutableList<MediaInfo>,
                    fileList: MutableMap<String, MediaInfo>
                ) {
                    pb_fg.visibility = View.GONE
                    imgAdapter.add(infoList)
                }
            }
            startScan()
        }
    }

    override fun onDestroy() {
        mMediaStorageManager.stopScan()
        super.onDestroy()
    }

    class ImgAdapter(context: Context): BaseRecyclerAdapter<MediaInfo>(context, R.layout.adapter_img_list, null){

        override fun bindData(holder: RecyclerViewHolder?, item: MediaInfo?, position: Int) {
            if (item!!.isVideo) {
                if (item.thumbnailBitmap == null) {
                    Glide.with(mActivity).load(item.thumbnailPath).into(holder!!.getImageView(R.id.iv_ail))
                }else{
                    Glide.with(mActivity).load(item.thumbnailBitmap).into(holder!!.getImageView(R.id.iv_ail))
                }
                holder.getTextView(R.id.tv_ail).visibility = View.VISIBLE
            }else{
                Glide.with(mActivity).load(item.path).into(holder!!.getImageView(R.id.iv_ail))
                holder.getTextView(R.id.tv_ail).visibility = View.INVISIBLE
            }
        }

    }
}