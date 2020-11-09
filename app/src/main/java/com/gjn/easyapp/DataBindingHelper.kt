package com.gjn.easyapp

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class DataBindingHelper {

    companion object{
        @BindingAdapter("drawImage")
        @JvmStatic
        fun drawImage(imageView: ImageView, any: Any?){
            Glide.with(imageView.context).load(any).into(imageView)
        }
    }

}