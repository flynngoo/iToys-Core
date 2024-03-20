package com.itoys.android.common.activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.drake.softinput.hideSoftInput
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.itoys.android.common.R
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.components.empty.EmptyLayout
import com.itoys.android.uikit.components.empty.addStateLayout
import com.itoys.android.uikit.initBar
import com.itoys.android.utils.expansion.then
import com.therouter.TheRouter

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/19
 */
abstract class AbsActivity<VB : ViewBinding> : AppCompatActivity() {

    /** activity 本身. */
    protected open var self: AppCompatActivity? = null

    /** view binding */
    protected open var binding: VB? = null

    /** Empty 空状态 */
    protected open var emptyLayout: EmptyLayout? = null

    /**
     * Empty - 重试
     */
    protected open val emptyLayoutRetry: () -> Unit = {
        logcat { ">>>>>>>>>> Empty - 重试 <<<<<<<<<<" }
    }

    /**
     * 标题栏
     */
    protected open var titleBar: TitleBar? = null

    /**
     * 标题栏点击时间
     */
    protected open var titleBarListener: OnTitleBarListener = object : OnTitleBarListener {
        override fun onLeftClick(titleBar: TitleBar?) {
            navBack()
        }
    }

    /**
     * 默认处理 Action Done事件.
     *
     * 隐藏输入框
     */
    protected open var dispatchActionDoneEvent = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        self = this
        TheRouter.inject(self)
        binding = createViewBinding()
        setContentView(binding?.root)
        supportActionBar?.hide()
        titleBar = findViewById(R.id.title_bar)
        titleBar?.setOnTitleBarListener(titleBarListener)

        customStatusBar()
        loadEmptyLayout()
        initialize(savedInstanceState)
        initData()
        addClickListen()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val currentView = currentFocus
            currentView?.let {
                it.clearFocus()
                hideSoftInput()
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    /**
     * 自定义状态栏
     *
     * 默认状态栏白色
     */
    protected open fun customStatusBar() {
        initBar()
    }

    /**
     * 创建ViewBinding.
     */
    abstract fun createViewBinding(): VB

    /**
     * 初始化view
     */
    abstract fun initialize(savedInstanceState: Bundle?)

    /**
     * 添加点击事件
     */
    open fun addClickListen() {
        // 返回拦截
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navBack()
            }
        })
    }

    /**
     * 返回
     */
    open fun navBack() {
        finish()
    }

    /**
     * 数据初始化
     */
    abstract fun initData()

    /**
     * 使用 empty layout
     */
    protected open fun useEmptyLayout() = true

    /**
     * 加载 state layout.
     */
    protected open fun loadEmptyLayout(view: View? = null) {
        if (useEmptyLayout()) {
            emptyLayout = (view == null).then({
                addStateLayout(retry = emptyLayoutRetry)
            }, {
                view?.addStateLayout(retry = emptyLayoutRetry)
            })
        }
    }

    /**
     * 侧滑finish activity
     */
    protected open fun useSwipeFinish(): Boolean = true
}