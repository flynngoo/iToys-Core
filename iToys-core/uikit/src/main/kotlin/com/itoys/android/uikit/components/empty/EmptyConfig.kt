package com.itoys.android.uikit.components.empty

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.itoys.android.uikit.R

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 15/04/2023
 * @desc 全局的缺省页布局配置
 */
class EmptyConfig private constructor(builder: Builder) {

    /** 加载中布局 */
    @LayoutRes
    private val loadingLayout: Int

    /** 空布局 */
    @LayoutRes
    private val emptyLayout: Int

    /** 空状态 - 自定义图片 */
    @DrawableRes
    private val emptyImage: Int

    /** 空状态 - 自定义描述 */
    @StringRes
    private val emptyDescText: Int

    /** 空状态 - 自定义错误图片 */
    @DrawableRes
    private val errorImage: Int

    /** 空状态 - 自定义错误描述 */
    @StringRes
    private val errorDescText: Int

    /** 重试 id list */
    private val retryIds: IntArray

    /** Empty changed handler */
    private val stateChangedHandler: StateChangedHandler

    init {
        this.loadingLayout = builder.loadingLayout
        this.emptyLayout = builder.emptyLayout
        this.emptyImage = builder.emptyImage
        this.emptyDescText = builder.emptyDescText
        this.errorImage = builder.errorImage
        this.errorDescText = builder.errorDescText
        this.retryIds = builder.retryIds
        this.stateChangedHandler = builder.stateChangedHandler
    }

    /**
     * 加载中布局
     */
    fun loadingLayout(): Int {
        return this.loadingLayout
    }

    /**
     * 加载中布局
     */
    fun emptyLayout(): Int {
        return this.emptyLayout
    }

    /**
     * Empty - 自定义图片
     */
    fun emptyImage(): Int {
        return emptyImage
    }

    /**
     * Empty - 自定义描述
     */
    fun emptyDescText(): Int {
        return emptyDescText
    }

    /**
     * Empty - 自定义错误图片
     */
    fun errorImage(): Int {
        return errorImage
    }

    /**
     * Empty - 自定义错误描述
     */
    fun errorDescText(): Int {
        return errorDescText
    }

    /**
     * 重试id list
     */
    fun retryIds(): IntArray {
        return this.retryIds
    }

    /**
     * 空状态改变处理
     */
    fun stateChangedHandler(): StateChangedHandler {
        return this.stateChangedHandler
    }

    class Builder {

        /** 加载中布局 */
        @LayoutRes
        var loadingLayout: Int = R.layout.uikit_layout_loading

        /** 空布局 */
        @LayoutRes
        var emptyLayout: Int = R.layout.uikit_layout_empty

        /** 空状态-自定义图片 */
        @DrawableRes
        var emptyImage: Int = R.drawable.uikit_icon_empty

        /** 空状态-自定义描述 */
        @StringRes
        var emptyDescText: Int = R.string.uikit_empty

        /** 空状态-自定义错误图片 */
        @DrawableRes
        var errorImage: Int = R.drawable.uikit_icon_empty

        /** 空状态-自定义错误描述 */
        @StringRes
        var errorDescText: Int = R.string.uikit_empty

        /** 重试 id list */
        var retryIds = intArrayOf(R.id.uikit_btn_retry)

        /** Empty changed handler */
        var stateChangedHandler: StateChangedHandler = StateChangedHandler

        /**
         * 加载页面 layoutId
         */
        fun setLoadingLayoutId(@LayoutRes layoutId: Int): Builder {
            loadingLayout = layoutId
            return this
        }

        /**
         * 空页面 layoutId
         */
        fun setEmptyLayoutId(@LayoutRes layoutId: Int): Builder {
            emptyLayout = layoutId
            return this
        }

        /**
         * 重试ids
         */
        fun setRetryIds(@IdRes vararg ids: Int): Builder {
            retryIds = ids
            return this
        }

        /**
         * State change handler.
         */
        fun setStateChangedHandler(handler: StateChangedHandler): Builder {
            stateChangedHandler = handler
            return this
        }

        fun build(): EmptyConfig {
            return EmptyConfig(builder = this)
        }
    }
}