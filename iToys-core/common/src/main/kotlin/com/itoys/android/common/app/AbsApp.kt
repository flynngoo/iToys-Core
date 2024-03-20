package com.itoys.android.common.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.itoys.android.uikit.UikitInitialization
import com.itoys.android.utils.SysUtils
import com.itoys.android.utils.expansion.launchOnIO
import com.therouter.TheRouter

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
abstract class AbsApp : Application() {

    override fun attachBaseContext(base: Context?) {
        TheRouter.isDebug = debugMode()
        super.attachBaseContext(base)
        // 如果项目方法数超过65536, 则需要使用MultiDex进行分包
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()

        if (SysUtils.isMainProcess(this)) {
            syncInit(application = this@AbsApp)

            launchOnIO {
                asyncInit(application = this@AbsApp)
            }

            // 注册activity 生命周期回调
            ActivityLifecycleImpl.install(application = this@AbsApp)
            // uikit 初始化
            UikitInitialization.initialization(application = this@AbsApp)
        }

        globalInit(application = this@AbsApp)
    }

    /**
     * 同步初始化.
     */
    abstract fun syncInit(application: Application)

    /**
     * 异步初始化
     */
    abstract suspend fun asyncInit(application: Application)

    /**
     * 全局初始化
     *
     * 例如：推送等进程
     */
    abstract fun globalInit(application: Application)

    /**
     * 合规初始化.
     *
     * 收集用户信息需要放到用户同意隐私政策之后
     */
    abstract fun initCompliance(application: Application)

    /**
     * debug 模式
     */
    abstract fun debugMode(): Boolean
}