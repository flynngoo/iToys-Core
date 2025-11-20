package com.itoys.android.image.engine

import android.content.Context
import android.net.Uri
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File
import java.util.ArrayList

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/20
 */
class CompressImageEngine : CompressFileEngine {

    companion object {
        private val INSTANCE: CompressImageEngine by lazy { CompressImageEngine() }

        fun create(): CompressImageEngine = INSTANCE
    }

    override fun onStartCompress(
        context: Context?,
        source: ArrayList<Uri>?,
        call: OnKeyValueResultCallbackListener?
    ) {
        Luban.with(context)
            .load(source)
            .ignoreBy(100)
            .setCompressListener(object : OnNewCompressListener {
                override fun onStart() {
                }

                override fun onSuccess(source: String?, compressFile: File?) {
                    call?.onCallback(source, compressFile?.absolutePath)
                }

                override fun onError(source: String?, e: Throwable?) {
                    call?.onCallback(source, null)
                }
            }).launch()
    }
}