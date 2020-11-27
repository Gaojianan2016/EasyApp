package com.gjn.easyapp

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.gjn.easyapp.base.BaseKtRecyclerAdapter
import com.gjn.easyapp.base.BaseVH
import com.gjn.easyapp.easybase.ABaseFragment
import com.gjn.easyapp.easyutils.gone
import com.gjn.easyapp.easyutils.invisible
import com.gjn.easyapp.easyutils.media.MediaInfo
import com.gjn.easyapp.easyutils.media.MediaStorageManager
import com.gjn.easyapp.easyutils.visible
import kotlinx.android.synthetic.main.adapter_img_list.view.*
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
//            isFindVideo = false
            scanCallback = object : MediaStorageManager.ScanCallback {
                override fun preStart() {
                    pb_fg.visible()
                }

                override fun complete(
                    infoList: MutableList<MediaInfo>,
                    fileList: MutableMap<String, MediaInfo>
                ) {
                    pb_fg.gone()
                    imgAdapter.add(infoList)
                }
            }
            filterListener = object : MediaStorageManager.FilterListener() {
                override fun onFilterPhoto(fileName: String): Boolean {
                    if (fileName.contains(".gif")) {
                        return true
                    }
                    return false
                }
            }
            startScan()
        }
    }

    override fun onDestroy() {
        mMediaStorageManager.stopScan()
        super.onDestroy()
    }

    class ImgAdapter(context: Context) :
        BaseKtRecyclerAdapter<MediaInfo>(context, R.layout.adapter_img_list) {

        override fun convertData(holder: BaseVH, item: MediaInfo, position: Int) {
            if (item.isVideo) {
                if (item.thumbnailBitmap == null) {
                    Glide.with(activity).load(item.thumbnailPath).into(holder.itemView.iv_ail)
                } else {
                    Glide.with(activity).load(item.thumbnailBitmap).into(holder.itemView.iv_ail)
                }
                holder.itemView.tv_ail.visible()
            } else {
                Glide.with(activity).load(item.path).into(holder.itemView.iv_ail)
                holder.itemView.tv_ail.invisible()
            }
        }

    }
}