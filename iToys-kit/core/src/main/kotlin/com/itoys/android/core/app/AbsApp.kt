package com.itoys.android.core.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.itoys.android.core.GlobalConfig
import com.itoys.android.core.network.NetworkInitialization
import com.itoys.android.logcat.LoggerInitialization
import com.itoys.android.uikit.UikitInitialization
import com.itoys.android.utils.SysUtils
import com.itoys.android.utils.UtilsInitialization
import com.itoys.android.utils.expansion.launchOnIO
import com.therouter.TheRouter

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
abstract class AbsApp : Application() {

    abstract val globalConfig: GlobalConfig

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (SysUtils.isMainProcess(this)) {
            // network 初始化
            NetworkInitialization.initialization(application = this@AbsApp, globalConfig = globalConfig)

            // logger 初始化
            LoggerInitialization.initialization(application = this@AbsApp, debug = globalConfig.debug)
        }

        // 如果项目方法数超过65536, 则需要使用MultiDex进行分包
        MultiDex.install(base)
        TheRouter.isDebug = globalConfig.debug
    }

    override fun onCreate() {
        super.onCreate()

        if (SysUtils.isMainProcess(this)) {
            // uikit 初始化
            UikitInitialization.initialization(application = this@AbsApp, imageFolder = globalConfig.imageFolder)
            // utils 初始化
            UtilsInitialization.initialization(application = this@AbsApp)
            // 注册activity 生命周期回调
            ActivityLifecycleImpl.install(application = this@AbsApp)

            syncInit(application = this@AbsApp)

            launchOnIO {
                asyncInit(application = this@AbsApp)
            }

            ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleMonitor.INSTANCE)
        }

        globalInit(application = this@AbsApp)

        // 全局异常捕获
        // CrashHandler.instance().initialization(this@AbsApp).setOnCrashListener(ToastCrashListener())
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
}