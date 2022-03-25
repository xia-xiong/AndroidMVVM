package com.android.libsBase.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.android.libsBase.BuildConfig
import com.android.libsBase.utils.Utils

/**
 * @author: 夏雄
 * @date: 2021/11/3
 */
open class BaseApplication  : Application(){
    companion object {
        lateinit var application: Application
    }

     var mActivityCount = 0
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        //注册监听每个activity的生命周期,便于堆栈式管理
        Utils.init(this)
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog() // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化

        registerActivityLifecycleCallbacks(mCallbacks)
    }


    private val mCallbacks: ActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            AppManager.getAppManager().addActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            mActivityCount++
        }

        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {
            mActivityCount--
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {
            AppManager.getAppManager().removeActivity(activity)
        }
    }
}