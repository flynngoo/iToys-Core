package com.itoys.android.uikit.components.empty

import android.content.Context
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textview.MaterialTextView
import com.itoys.android.uikit.R
import com.itoys.android.uikit.UikitBridge
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.tagName
import com.itoys.android.utils.expansion.then

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 14/04/2023
 * @desc 全局缺省页
 */
class EmptyLayout(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val statusContainer: ArrayMap<EmptyStatus, StateInfo> by lazy { ArrayMap() }

    var currStatus: EmptyStatus = EmptyStatus.CONTENT
    private var isShowRetry: Boolean = true
    private var isShowRetryOnEmpty: Boolean = false

    /** 处理缺省页状态变更 */
    private var stateChangedHandler: StateChangedHandler = UikitBridge.getEmptyChangedHandler()

    /** 加载中动画 */
    private var loadingLottieView: LottieAnimationView? = null

    /** 是否成功加载过 */
    private var isLoaded = false

    /**
     * 重试ids
     */
    private var retryIds: IntArray? = null

    /**
     * 加载页面 layoutId
     */
    @LayoutRes
    private var loadingLayoutId: Int = View.NO_ID

    /**
     * 空页面 layoutId
     */
    @LayoutRes
    private var emptyLayoutId: Int = View.NO_ID

    /** 空状态-自定义图片 */
    @DrawableRes
    private var emptyImage: Int = View.NO_ID

    /** 空状态-自定义描述 */
    @StringRes
    private var emptyDescText: Int = View.NO_ID

    /** 空状态-自定义错误图片 */
    @DrawableRes
    private var errorImage: Int = View.NO_ID

    /** 空状态-自定义错误描述 */
    @StringRes
    private var errorDescText: Int = View.NO_ID

    /**
     * 重试 点击事件
     */
    private var onRetry: (() -> Unit)? = null

    /**
     * 加载页面 layoutId
     */
    fun setLoadingLayoutId(@LayoutRes layoutId: Int) {
        this.loadingLayoutId = layoutId
    }

    /**
     * 空页面 layoutId
     */
    fun setEmptyLayoutId(@LayoutRes layoutId: Int) {
        this.emptyLayoutId = layoutId
    }

    fun setEmptyImage(@DrawableRes image: Int) {
        this.emptyImage = image
    }

    fun setEmptyDescText(@StringRes text: Int) {
        this.emptyDescText = text
    }

    fun setErrorImage(@DrawableRes image: Int) {
        this.errorImage = image
    }

    fun setErrorDescText(@StringRes text: Int) {
        this.errorDescText = text
    }

    /**
     * 重试ids
     */
    fun setRetryIds(ids: IntArray?) {
        this.retryIds = ids
    }

    /**
     * 重试回调
     */
    fun setRetry(retry: () -> Unit) {
        this.onRetry = retry
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 1 || childCount == 0) {
            throw UnsupportedOperationException("StateLayout only have one child view")
        }

        if (statusContainer.isEmpty()) {
            val view = getChildAt(0)
            setContentView(view)
        }
    }

    fun showStatus(status: EmptyStatus?, tag: String? = null) {
        if (status == currStatus || status == null) return

        val preStatus = this.currStatus
        val targetStatusView = getStatusView(status, tag) ?: return

        stateChangedHandler.onAdd(
            this@EmptyLayout,
            targetStatusView,
            status,
            tag
        )

        playLoadingLottie(targetStatusView, status)

        statusContainer.filter { it.key == preStatus }.forEach { entry ->
            stateChangedHandler.onRemove(
                this@EmptyLayout,
                entry.value.view,
                entry.key,
                entry.value.tag
            )
        }

        this.currStatus = status

        retryIds?.forEach { retryId ->
            val retryView = targetStatusView.findViewById(retryId) as View?

            retryView?.visibility = if (this.currStatus == EmptyStatus.EMPTY) {
                isShowRetryOnEmpty.then(View.VISIBLE, View.GONE)
            } else {
                isShowRetry.then(View.VISIBLE, View.GONE)
            }

            retryView?.doOnClick {
                onRetry?.invoke()
            }
        }

        val stateImage = targetStatusView.findViewById(R.id.uikit_iv_state) as AppCompatImageView?
        val stateText = targetStatusView.findViewById(R.id.uikit_tv_state) as MaterialTextView?

        when (this.currStatus) {
            EmptyStatus.EMPTY -> {
                stateImage?.setImageResource(emptyImage)
                stateText?.setText(emptyDescText)
            }

            EmptyStatus.ERROR -> {
                stateImage?.setImageResource(errorImage)
                stateText?.setText(errorDescText)
            }

            else -> {}
        }
    }

    /**
     * 设置 content view
     */
    fun setContentView(view: View) {
        statusContainer[EmptyStatus.CONTENT] = StateInfo(view, view.javaClass.tagName)
    }

    /**
     * 是否显示重试按钮
     */
    fun showRetryButton(showRetry: Boolean) {
        this.isShowRetry = showRetry
    }

    /**
     * 空布局是否显示重试按钮
     */
    fun showRetryButtonOnEmpty(showRetry: Boolean) {
        this.isShowRetryOnEmpty = showRetry
    }

    /**
     * 显示加载中Page
     */
    fun showLoading(tag: String? = null) {
        showStatus(EmptyStatus.LOADING, tag)
    }

    /**
     * 显示空Page
     */
    fun showEmpty(tag: String? = null) {
        showStatus(EmptyStatus.EMPTY, tag)
    }

    /**
     * 显示错误Page
     */
    fun showError(tag: String? = null, isNetError: Boolean = false) {
        showStatus(isNetError.then(EmptyStatus.NET_ERROR, EmptyStatus.ERROR), tag)
    }

    fun showContent(tag: String? = null) {
        showStatus(EmptyStatus.CONTENT, tag)
        isLoaded = true
    }

    private fun getStatusView(status: EmptyStatus, tag: String?): View? {
        statusContainer[status]?.let { info ->
            info.tag = tag
            return info.view
        }

        val layoutId = when (status) {
            EmptyStatus.LOADING -> loadingLayoutId

            EmptyStatus.EMPTY,
            EmptyStatus.ERROR,
            EmptyStatus.NET_ERROR -> emptyLayoutId

            EmptyStatus.CONTENT -> View.NO_ID
        }

        if (layoutId == View.NO_ID) return null

        val stateLayoutView = LayoutInflater.from(context).inflate(layoutId, this, false)
        statusContainer[status] = StateInfo(stateLayoutView, tag)
        return stateLayoutView
    }

    private fun playLoadingLottie(loadingView: View?, status: EmptyStatus) {
        if (status != EmptyStatus.LOADING || loadingView == null) return

        if (loadingLottieView == null) {
            loadingLottieView = loadingView.findViewById(R.id.uikit_lottie_loading)
            loadingLottieView?.setAnimation("empty/loading.json")
        }

        loadingLottieView?.progress = 0f
        loadingLottieView?.playAnimation()
    }
}