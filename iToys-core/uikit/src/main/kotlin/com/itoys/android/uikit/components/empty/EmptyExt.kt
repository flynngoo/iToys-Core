package com.itoys.android.uikit.components.empty

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.itoys.android.uikit.UikitBridge

/**
 *
 * 添加空布局
 *
 * [loadingLayoutId] 加载中布局
 * [emptyLayoutId] 空布局
 * [emptyImage] 空图片
 * [emptyDescText] 空描述
 * [errorImage] 错误图片
 * [errorDescText] 错误描述
 * [retryIds] 重试 ids
 * [showRetryOnEmpty] 空布局是否显示重试按钮
 * [showRetry] 是否显示重试按钮
 * [retry] 重试回调
 *
 */
fun Activity.addStateLayout(
    @LayoutRes loadingLayoutId: Int = UikitBridge.emptyLoadingLayout(),
    @LayoutRes emptyLayoutId: Int = UikitBridge.emptyLayout(),
    @DrawableRes emptyImage: Int = UikitBridge.emptyImage(),
    @StringRes emptyDescText: Int = UikitBridge.emptyDescText(),
    @DrawableRes errorImage: Int = UikitBridge.emptyErrorImage(),
    @StringRes errorDescText: Int = UikitBridge.emptyErrorDescText(),
    retryIds: IntArray? = UikitBridge.emptyRetryIds(),
    showRetryOnEmpty: Boolean = false,
    showRetry: Boolean = true,
    retry: () -> Unit = {}
): EmptyLayout {
    val contentView = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    return contentView.addStateLayout(
        loadingLayoutId,
        emptyLayoutId,
        emptyImage,
        emptyDescText,
        errorImage,
        errorDescText,
        retryIds,
        showRetryOnEmpty,
        showRetry,
        retry
    )
}

/**
 *
 * 添加空布局
 *
 * [loadingLayoutId] 加载中布局
 * [emptyLayoutId] 空布局
 * [emptyImage] 空图片
 * [emptyDescText] 空描述
 * [errorImage] 错误图片
 * [errorDescText] 错误描述
 * [retryIds] 重试 ids
 * [showRetryOnEmpty] 空布局是否显示重试按钮
 * [showRetry] 是否显示重试按钮
 * [retry] 重试回调
 *
 */
fun Fragment.addStateLayout(
    @LayoutRes loadingLayoutId: Int = UikitBridge.emptyLoadingLayout(),
    @LayoutRes emptyLayoutId: Int = UikitBridge.emptyLayout(),
    @DrawableRes emptyImage: Int = UikitBridge.emptyImage(),
    @StringRes emptyDescText: Int = UikitBridge.emptyDescText(),
    @DrawableRes errorImage: Int = UikitBridge.emptyErrorImage(),
    @StringRes errorDescText: Int = UikitBridge.emptyErrorDescText(),
    retryIds: IntArray? = UikitBridge.emptyRetryIds(),
    showRetryOnEmpty: Boolean = false,
    showRetry: Boolean = true,
    retry: () -> Unit = {}
): EmptyLayout {
    val stateLayout = requireView().addStateLayout(
        loadingLayoutId,
        emptyLayoutId,
        emptyImage,
        emptyDescText,
        errorImage,
        errorDescText,
        retryIds,
        showRetryOnEmpty,
        showRetry,
        retry
    )

    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            val parent = stateLayout.parent as ViewGroup?
            parent?.removeView(stateLayout)
            lifecycle.removeObserver(this)
            super.onDestroy(owner)
        }
    })

    return stateLayout
}

/**
 *
 * 添加空布局
 *
 * [loadingLayoutId] 加载中布局
 * [emptyLayoutId] 空布局
 * [emptyImage] 空图片
 * [emptyDescText] 空描述
 * [errorImage] 错误图片
 * [errorDescText] 错误描述
 * [retryIds] 重试 ids
 * [showRetryOnEmpty] 空布局是否显示重试按钮
 * [showRetry] 是否显示重试按钮
 * [retry] 重试回调
 *
 */
fun View.addStateLayout(
    @LayoutRes loadingLayoutId: Int = UikitBridge.emptyLoadingLayout(),
    @LayoutRes emptyLayoutId: Int = UikitBridge.emptyLayout(),
    @DrawableRes emptyImage: Int = UikitBridge.emptyImage(),
    @StringRes emptyDescText: Int = UikitBridge.emptyDescText(),
    @DrawableRes errorImage: Int = UikitBridge.emptyErrorImage(),
    @StringRes errorDescText: Int = UikitBridge.emptyErrorDescText(),
    retryIds: IntArray? = UikitBridge.emptyRetryIds(),
    showRetryOnEmpty: Boolean = false,
    showRetry: Boolean = true,
    retry: () -> Unit = {}
): EmptyLayout {
    val parent = parent as ViewGroup
    if (parent is ViewPager || parent is RecyclerView) {
        throw UnsupportedOperationException("You should using StateLayout wrap [ $this ] in layout when parent is ViewPager or RecyclerView")
    }

    val viewIndex = parent.indexOfChild(this)
    val stateLayout = EmptyLayout(context)
    stateLayout.id = View.generateViewId()
    stateLayout.setLoadingLayoutId(loadingLayoutId)
    stateLayout.setEmptyLayoutId(emptyLayoutId)
    stateLayout.setEmptyImage(emptyImage)
    stateLayout.setEmptyDescText(emptyDescText)
    stateLayout.setErrorImage(errorImage)
    stateLayout.setErrorDescText(errorDescText)
    stateLayout.setRetryIds(retryIds)
    stateLayout.setRetry(retry)

    val stateLayoutParams = layoutParams
    parent.removeView(this)
    parent.addView(stateLayout, viewIndex, stateLayoutParams)

    when (this) {
        is ConstraintLayout -> {
            val contentViewLayoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            stateLayout.addView(this, contentViewLayoutParams)
        }

        else -> {
            stateLayout.addView(this)
        }
    }

    stateLayout.showRetryButton(showRetry)
    stateLayout.showRetryButtonOnEmpty(showRetryOnEmpty)
    stateLayout.setContentView(this)
    return stateLayout
}