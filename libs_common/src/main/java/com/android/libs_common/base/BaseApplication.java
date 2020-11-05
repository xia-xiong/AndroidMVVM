package com.android.libs_common.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.multidex.MultiDex;

import com.android.libs_common.utils.Utils;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;


/**
 *
 */

public class BaseApplication extends Application {
    private static BaseApplication sInstance;
    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mContext = getApplicationContext();
        Utils.init(this);
        //注册监听每个activity的生命周期,便于堆栈式管理
        registerActivityLifecycleCallbacks(mCallbacks);
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {//OnErrorNotImplementedException 的处理
            @Override
            public void accept(Throwable throwable) {
                //异常处理
            }
        });
    }

    /**
     * 获得当前app运行的AppContext
     */
    public static BaseApplication getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return mContext;
    }

    private int mActivityCount = 0;

    public int getActivityCount() {
        return mActivityCount;
    }

    private ActivityLifecycleCallbacks mCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            AppManager.getAppManager().addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (mActivityCount == 0) {
//                RxBus.getDefault().post(new CommRxBusBean(CommRxBusCode.APP_FOREGROUND, true));
            }
            mActivityCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            mActivityCount--;
            if (mActivityCount == 0) {
//                RxBus.getDefault().post(new CommRxBusBean(CommRxBusCode.APP_FOREGROUND, false));
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            AppManager.getAppManager().removeActivity(activity);
        }
    };
}
