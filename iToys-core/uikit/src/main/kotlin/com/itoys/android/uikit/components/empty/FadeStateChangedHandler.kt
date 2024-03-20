package com.itoys.android.uikit.components.empty

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.itoys.android.uikit.R
import java.lang.ref.WeakReference

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 16/04/2023
 * @desc 切换状态时使用渐变透明动画过渡
 */
open class FadeStateChangedHandler(
    private val duration: Long = 300
) : StateChangedHandler {

    private var stateLayout: WeakReference<EmptyLayout> = WeakReference(null)

    override fun onAdd(container: EmptyLayout, stateView: View, status: EmptyStatus, tag: String?) {
        if (container != stateLayout.get()) {
            stateLayout = WeakReference(container)
            return super.onAdd(container, stateView, status, tag)
        }

        super.onAdd(container, stateView, status, tag)
        stateView.alpha = 0f
        stateView.animate().setDuration(duration).alpha(1f).start()
    }

    override fun onRemove(
        container: EmptyLayout,
        stateView: View,
        status: EmptyStatus,
        tag: String?
    ) {
        if (status == EmptyStatus.LOADING) {
            val loadingLottieView = stateView.findViewById(R.id.uikit_lottie_loading) as LottieAnimationView?
            loadingLottieView?.cancelAnimation()
        }

        if (container != stateLayout.get()) {
            return super.onRemove(container, stateView, status, tag)
        }

        stateView.animate().setDuration(duration).alpha(0f).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // 等待动画执行完毕后删除旧的缺省页视图
                StateChangedHandler.onRemove(container, stateView, status, tag)
            }
        }).start()
    }
}