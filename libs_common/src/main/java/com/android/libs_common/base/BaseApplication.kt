package com.android.libs_common.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.android.libs_common.utils.Utils

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