package com.gjn.easyapp

import android.content.Context
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.gjn.easyapp.easybase.ABaseFragment
import com.gjn.easyapp.easydialoger.EasyDialogUtils
import com.gjn.universaladapterlibrary.BaseRecyclerAdapter
import com.gjn.universaladapterlibrary.RecyclerViewHolder
import kotlinx.android.synthetic.main.fragment_gallery.*


class GalleryFragment : ABaseFragment() {

    private val mLoaderCallback by lazy { MediaLoaderCallback(mActivity) }
    private val mLoaderCallback2 by lazy { MediaLoaderCallback(mActivity) }

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
//        LoaderManager.getInstance(mActivity).initLoader(LOADER_PHOTO, null, mLoaderCallback)
//
//        mLoaderCallback.callback = object : MediaLoaderCallback.Callback{
//            override fun complete(
//                infos: MutableList<MediaInfo>,
//                files: MutableMap<String, MediaInfo>
//            ) {
//                imgAdapter.add(infos)
//            }
//        }
//        LoaderManager.getInstance(mActivity).initLoader(LOADER_VIDEO, null, mLoaderCallback2)
//        mLoaderCallback2.callback = object : MediaLoaderCallback.Callback{
//            override fun complete(
//                infos: MutableList<MediaInfo>,
//                files: MutableMap<String, MediaInfo>
//            ) {
//                imgAdapter.add(infos)
//            }
//        }

        mMediaStorageManager.run {
            callback = object : MediaStorageManager.Callback{
                override fun complete(
                    infoList: MutableList<MediaInfo>,
                    fileList: MutableMap<String, MediaInfo>
                ) {
                    imgAdapter.add(infoList)
                }
            }
            startScan()
        }
    }

    class ImgAdapter(context: Context): BaseRecyclerAdapter<MediaInfo>(context, R.layout.adapter_img_list, null){

        override fun bindData(holder: RecyclerViewHolder?, item: MediaInfo?, position: Int) {
            if (item!!.isVideo) {
                if (item.thumbnailBitmap == null) {
                    Glide.with(mActivity).load(item.thumbnailPath).into(holder!!.getImageView(R.id.iv_ail))
                }else{
                    Glide.with(mActivity).load(item.thumbnailBitmap).into(holder!!.getImageView(R.id.iv_ail))
                }
            }else{
                Glide.with(mActivity).load(item.path).into(holder!!.getImageView(R.id.iv_ail))
            }
        }

    }

    companion object {

    }
}