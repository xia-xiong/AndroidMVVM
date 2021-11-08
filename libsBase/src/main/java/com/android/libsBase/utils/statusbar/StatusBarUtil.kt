package com.android.libsBase.utils.statusbar

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.libsBase.R
import java.util.regex.Pattern

/**
 * Created by chenxz on 2018/4/21.
 * 沉浸式状态栏工具类
 *
 */
object StatusBarUtil {

    val DEFAULT_STATUS_BAR_ALPHA = 0
    private val FAKE_STATUS_BAR_VIEW_ID = R.id.statusbarutil_fake_status_bar_view
    private val FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view
    private val TAG_KEY_HAVE_SET_OFFSET = -123

    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    fun setColor(activity: Activity, @ColorInt color: Int) {
        setColor(
            activity,
            color,
            DEFAULT_STATUS_BAR_ALPHA
        )
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity       需要设置的activity
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度
     */

    fun setColor(
        activity: Activity, @ColorInt color: Int, @IntRange(
            from = 0,
            to = 255
        ) statusBarAlpha: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor =
                calculateStatusColor(
                    color,
                    statusBarAlpha
                )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val decorView = activity.window.decorView as ViewGroup
            val fakeStatusBarView = decorView.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
            if (fakeStatusBarView != null) {
                if (fakeStatusBarView!!.visibility === View.GONE) {
                    fakeStatusBarView!!.visibility = View.VISIBLE
                }
                fakeStatusBarView!!.setBackgroundColor(
                    calculateStatusColor(
                        color,
                        statusBarAlpha
                    )
                )
            } else {
                decorView.addView(
                    createStatusBarView(
                        activity,
                        color,
                        statusBarAlpha
                    )
                )
            }
            setRootView(
                activity
            )
        }
    }

    /**
     * 为滑动返回界面设置状态栏颜色
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     */
    fun setColorForSwipeBack(activity: Activity, color: Int) {
        setColorForSwipeBack(
            activity,
            color,
            DEFAULT_STATUS_BAR_ALPHA
        )
    }

    /**
     * 为滑动返回界面设置状态栏颜色
     *
     * @param activity       需要设置的activity
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度 @IntRange(from = 0, to = 255)
     */
    fun setColorForSwipeBack(activity: Activity, @ColorInt color: Int, statusBarAlpha: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            val contentView = activity.findViewById<View>(android.R.id.content) as ViewGroup
            val rootView = contentView.getChildAt(0)
            val statusBarHeight =
                getStatusBarHeight(
                    activity
                )
            if (rootView != null && rootView is CoordinatorLayout) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    rootView.fitsSystemWindows = false
                    contentView.setBackgroundColor(
                        calculateStatusColor(
                            color,
                            statusBarAlpha
                        )
                    )
                    val isNeedRequestLayout = contentView.paddingTop < statusBarHeight
                    if (isNeedRequestLayout) {
                        contentView.setPadding(0, statusBarHeight, 0, 0)
                        rootView.post { rootView.requestLayout() }
                    }
                } else {
                    rootView.setStatusBarBackgroundColor(
                        calculateStatusColor(
                            color,
                            statusBarAlpha
                        )
                    )
                }
            } else {
                contentView.setPadding(0, statusBarHeight, 0, 0)
                contentView.setBackgroundColor(
                    calculateStatusColor(
                        color,
                        statusBarAlpha
                    )
                )
            }
            setTransparentForWindow(
                activity
            )
        }
    }

    /**
     * 设置状态栏纯色 不加半透明效果
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    fun setColorNoTranslucent(activity: Activity, @ColorInt color: Int) {
        setColor(
            activity,
            color,
            0
        )
    }

    /**
     * 设置状态栏颜色(5.0以下无半透明效果,不建议使用)
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    @Deprecated("")
    fun setColorDiff(activity: Activity, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        transparentStatusBar(
            activity
        )
        val contentView = activity.findViewById<View>(android.R.id.content) as ViewGroup
        // 移除半透明矩形,以免叠加
        val fakeStatusBarView = contentView.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView!!.visibility === View.GONE) {
                fakeStatusBarView!!.visibility = View.VISIBLE
            }
            fakeStatusBarView!!.setBackgroundColor(color)
        } else {
            contentView.addView(
                createStatusBarView(
                    activity,
                    color
                )
            )
        }
        setRootView(
            activity
        )
    }

    /**
     * 使状态栏半透明
     *
     *
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */
    fun setTranslucent(activity: Activity) {
        setTranslucent(
            activity,
            DEFAULT_STATUS_BAR_ALPHA
        )
    }

    /**
     * 使状态栏半透明
     *
     *
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity       需要设置的activity
     * @param statusBarAlpha 状态栏透明度 @IntRange(from = 0, to = 255)
     */
    fun setTranslucent(activity: Activity, statusBarAlpha: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        setTransparent(
            activity
        )
        addTranslucentView(
            activity,
            statusBarAlpha
        )
    }

    /**
     * 针对根布局是 CoordinatorLayout, 使状态栏半透明
     *
     *
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity       需要设置的activity
     * @param statusBarAlpha 状态栏透明度 @IntRange(from = 0, to = 255)
     */
    fun setTranslucentForCoordinatorLayout(activity: Activity, statusBarAlpha: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        transparentStatusBar(
            activity
        )
        addTranslucentView(
            activity,
            statusBarAlpha
        )
    }

    /**
     * 设置状态栏全透明
     *
     * @param activity 需要设置的activity
     */
    fun setTransparent(activity: Activity) {
        transparentStatusBar(
            activity
        )
        setRootView(
            activity
        )
    }

    /**
     * 使状态栏透明(5.0以上半透明效果,不建议使用)
     *
     *
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */
    @Deprecated("")
    fun setTranslucentDiff(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            setRootView(
                activity
            )
        }
    }

    /**
     * 为DrawerLayout 布局设置状态栏变色
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     * @param color        状态栏颜色值
     */
    fun setColorForDrawerLayout(
        activity: Activity,
        drawerLayout: DrawerLayout, @ColorInt color: Int
    ) {
        setColorForDrawerLayout(
            activity,
            drawerLayout,
            color,
            DEFAULT_STATUS_BAR_ALPHA
        )
    }

    /**
     * 为DrawerLayout 布局设置状态栏颜色,纯色
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     * @param color        状态栏颜色值
     */
    fun setColorNoTranslucentForDrawerLayout(
        activity: Activity,
        drawerLayout: DrawerLayout, @ColorInt color: Int
    ) {
        setColorForDrawerLayout(
            activity,
            drawerLayout,
            color,
            0
        )
    }

    /**
     * 为DrawerLayout 布局设置状态栏变色
     *
     * @param activity       需要设置的activity
     * @param drawerLayout   DrawerLayout
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度 @IntRange(from = 0, to = 255)
     */
    fun setColorForDrawerLayout(
        activity: Activity, drawerLayout: DrawerLayout, @ColorInt color: Int,
        statusBarAlpha: Int
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        // 生成一个状态栏大小的矩形
        // 添加 statusBarView 到布局中
        val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
        val fakeStatusBarView = contentLayout.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView!!.visibility === View.GONE) {
                fakeStatusBarView!!.visibility = View.VISIBLE
            }
            fakeStatusBarView!!.setBackgroundColor(color)
        } else {
            contentLayout.addView(
                createStatusBarView(
                    activity,
                    color
                ), 0)
        }
        // 内容布局不是 LinearLayout 时,设置padding top
        if (contentLayout !is LinearLayout && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1)
                .setPadding(
                    contentLayout.paddingLeft,
                    getStatusBarHeight(
                        activity
                    ) + contentLayout.paddingTop,
                    contentLayout.paddingRight,
                    contentLayout.paddingBottom
                )
        }
        // 设置属性
        setDrawerLayoutProperty(
            drawerLayout,
            contentLayout
        )
        addTranslucentView(
            activity,
            statusBarAlpha
        )
    }

    /**
     * 设置 DrawerLayout 属性
     *
     * @param drawerLayout              DrawerLayout
     * @param drawerLayoutContentLayout DrawerLayout 的内容布局
     */
    private fun setDrawerLayoutProperty(
        drawerLayout: DrawerLayout,
        drawerLayoutContentLayout: ViewGroup
    ) {
        val drawer = drawerLayout.getChildAt(1) as ViewGroup
        drawerLayout.fitsSystemWindows = false
        drawerLayoutContentLayout.fitsSystemWindows = false
        drawerLayoutContentLayout.clipToPadding = true
        drawer.fitsSystemWindows = false
    }

    /**
     * 为DrawerLayout 布局设置状态栏变色(5.0以下无半透明效果,不建议使用)
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     * @param color        状态栏颜色值
     */
    @Deprecated("")
    fun setColorForDrawerLayoutDiff(
        activity: Activity,
        drawerLayout: DrawerLayout, @ColorInt color: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 生成一个状态栏大小的矩形
            val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
            val fakeStatusBarView = contentLayout.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
            if (fakeStatusBarView != null) {
                if (fakeStatusBarView.visibility === View.GONE) {
                    fakeStatusBarView.visibility = View.VISIBLE
                }
                fakeStatusBarView!!.setBackgroundColor(
                    calculateStatusColor(
                        color,
                        DEFAULT_STATUS_BAR_ALPHA
                    )
                )
            } else {
                // 添加 statusBarView 到布局中
                contentLayout.addView(
                    createStatusBarView(
                        activity,
                        color
                    ), 0)
            }
            // 内容布局不是 LinearLayout 时,设置padding top
            if (contentLayout !is LinearLayout && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0,
                    getStatusBarHeight(
                        activity
                    ), 0, 0)
            }
            // 设置属性
            setDrawerLayoutProperty(
                drawerLayout,
                contentLayout
            )
        }
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     */
    fun setTranslucentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout) {
        setTranslucentForDrawerLayout(
            activity,
            drawerLayout,
            DEFAULT_STATUS_BAR_ALPHA
        )
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     */
    fun setTranslucentForDrawerLayout(
        activity: Activity,
        drawerLayout: DrawerLayout,
        statusBarAlpha: Int
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        setTransparentForDrawerLayout(
            activity,
            drawerLayout
        )
        addTranslucentView(
            activity,
            statusBarAlpha
        )
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     */
    fun setTransparentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
        // 内容布局不是 LinearLayout 时,设置padding top
        if (contentLayout !is LinearLayout && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1).setPadding(0,
                getStatusBarHeight(
                    activity
                ), 0, 0)
        }

        // 设置属性
        setDrawerLayoutProperty(
            drawerLayout,
            contentLayout
        )
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明(5.0以上半透明效果,不建议使用)
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     */
    @Deprecated("")
    fun setTranslucentForDrawerLayoutDiff(activity: Activity, drawerLayout: DrawerLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 设置内容布局属性
            val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
            contentLayout.fitsSystemWindows = true
            contentLayout.clipToPadding = true
            // 设置抽屉布局属性
            val vg = drawerLayout.getChildAt(1) as ViewGroup
            vg.fitsSystemWindows = false
            // 设置 DrawerLayout 属性
            drawerLayout.fitsSystemWindows = false
        }
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏全透明
     *
     * @param activity       需要设置的activity
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTransparentForImageView(activity: Activity, needOffsetView: View) {
        setTranslucentForImageView(
            activity,
            0,
            needOffsetView
        )
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏透明(使用默认透明度)
     *
     * @param activity       需要设置的activity
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTranslucentForImageView(activity: Activity, needOffsetView: View) {
        setTranslucentForImageView(
            activity,
            DEFAULT_STATUS_BAR_ALPHA,
            needOffsetView
        )
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏透明
     *
     * @param activity       需要设置的activity
     * @param statusBarAlpha 状态栏透明度
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTranslucentForImageView(
        activity: Activity, statusBarAlpha: Int,
        needOffsetView: View?
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        setTransparentForWindow(
            activity
        )
        addTranslucentView(
            activity,
            statusBarAlpha
        )
        if (needOffsetView != null) {
            val haveSetOffset = needOffsetView!!.getTag(TAG_KEY_HAVE_SET_OFFSET)
            if (haveSetOffset != null && haveSetOffset as Boolean) {
                return
            }
            val layoutParams = needOffsetView!!.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(
                layoutParams.leftMargin, layoutParams.topMargin + getStatusBarHeight(
                    activity
                ),
                layoutParams.rightMargin, layoutParams.bottomMargin
            )
            needOffsetView!!.setTag(TAG_KEY_HAVE_SET_OFFSET, true)
        }
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param activity       fragment 对应的 activity
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTranslucentForImageViewInFragment(activity: Activity, needOffsetView: View?) {
        setTranslucentForImageViewInFragment(
            activity,
            DEFAULT_STATUS_BAR_ALPHA,
            needOffsetView
        )
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param activity       fragment 对应的 activity
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTransparentForImageViewInFragment(activity: Activity, needOffsetView: View) {
        setTranslucentForImageViewInFragment(
            activity,
            0,
            needOffsetView
        )
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param activity       fragment 对应的 activity
     * @param statusBarAlpha 状态栏透明度
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTranslucentForImageViewInFragment(
        activity: Activity, statusBarAlpha: Int,
        needOffsetView: View?
    ) {
        setTranslucentForImageView(
            activity,
            statusBarAlpha,
            needOffsetView
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            clearPreviousSetting(
                activity
            )
        }
    }

    /**
     * 隐藏伪状态栏 View
     *
     * @param activity 调用的 Activity
     */
    fun hideFakeStatusBarView(activity: Activity) {
        val decorView = activity.window.decorView as ViewGroup
        val fakeStatusBarView = decorView.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
        if (fakeStatusBarView != null) {
            fakeStatusBarView!!.setVisibility(View.GONE)
        }
        val fakeTranslucentView = decorView.findViewById<View>(FAKE_TRANSLUCENT_VIEW_ID)
        if (fakeTranslucentView != null) {
            fakeTranslucentView!!.setVisibility(View.GONE)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun setLightMode(activity: Activity) {
        setMIUIStatusBarDarkIcon(
            activity,
            true
        )
        setMeizuStatusBarDarkIcon(
            activity,
            true
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun setDarkMode(activity: Activity) {
        setMIUIStatusBarDarkIcon(
            activity,
            false
        )
        setMeizuStatusBarDarkIcon(
            activity,
            false
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    /**
     * 修改 MIUI V6  以上状态栏颜色
     */
    @SuppressLint("PrivateApi")
    private fun setMIUIStatusBarDarkIcon(activity: Activity, darkIcon: Boolean) {
        val clazz = activity.window.javaClass
        try {
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.invoke(activity.window, if (darkIcon) darkModeFlag else 0, darkModeFlag)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 修改魅族状态栏字体颜色 Flyme 4.0
     */
    private fun setMeizuStatusBarDarkIcon(activity: Activity, darkIcon: Boolean) {
        try {
            val lp = activity.window.attributes
            val darkFlag =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            if (darkIcon) {
                value = value or bit
            } else {
                value = value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            activity.window.attributes = lp
        } catch (e: Exception) {
            e.printStackTrace();
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun clearPreviousSetting(activity: Activity) {
        val decorView = activity.window.decorView as ViewGroup
        val fakeStatusBarView = decorView.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
        if (fakeStatusBarView != null) {
            decorView.removeView(fakeStatusBarView)
            val rootView =
                (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
            rootView.setPadding(0, 0, 0, 0)
        }
    }

    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private fun addTranslucentView(activity: Activity, statusBarAlpha: Int) {
        val contentView = activity.findViewById<View>(android.R.id.content) as ViewGroup
        val fakeTranslucentView = contentView.findViewById<View>(FAKE_TRANSLUCENT_VIEW_ID)
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView!!.visibility === View.GONE) {
                fakeTranslucentView!!.visibility = View.VISIBLE
            }
            fakeTranslucentView!!.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0))
        } else {
            contentView.addView(
                createTranslucentStatusBarView(
                    activity,
                    statusBarAlpha
                )
            )
        }
    }

    /**
     * 生成一个和状态栏大小相同的彩色矩形条
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private fun createStatusBarView(activity: Activity, @ColorInt color: Int): View {
        return createStatusBarView(
            activity,
            color,
            0
        )
    }

    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @param alpha    透明值
     * @return 状态栏矩形条
     */
    private fun createStatusBarView(
        activity: Activity, @ColorInt color: Int, @IntRange(
            from = 0,
            to = 255
        ) alpha: Int
    ): View {
        // 绘制一个和状态栏一样高的矩形
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(
                activity
            )
        )
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(
            calculateStatusColor(
                color,
                alpha
            )
        )
        statusBarView.id =
            FAKE_STATUS_BAR_VIEW_ID
        return statusBarView
    }

    /**
     * 设置根布局参数
     */
    private fun setRootView(activity: Activity) {
        val parent = activity.findViewById<View>(android.R.id.content) as ViewGroup
        var i = 0
        val count = parent.childCount
        while (i < count) {
            val childView = parent.getChildAt(i)
            if (childView is ViewGroup) {
                childView.setFitsSystemWindows(true)
                childView.clipToPadding = true
            }
            i++
        }
    }

    /**
     * 设置透明
     */
    private fun setTransparentForWindow(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window
                .decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window
                .setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )
        }
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun transparentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun translucentStatusBar(
        activity: Activity,
        hideStatusBarBackground: Boolean
    ) {
        val window = activity.window
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (hideStatusBarBackground) {
            //如果为全透明模式，取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //设置状态栏为透明
            window.statusBarColor = Color.TRANSPARENT
            //设置window的状态栏不可见
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            //            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        //view不根据系统窗口来调整自己的布局
        val mContentView =
            window.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup
        val mChildView = mContentView.getChildAt(0)
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false)
            ViewCompat.requestApplyInsets(mChildView)
        }
    }

    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private fun createTranslucentStatusBarView(
        activity: Activity, @IntRange(
            from = 0,
            to = 255
        ) alpha: Int
    ): View {
        // 绘制一个和状态栏一样高的矩形
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(
                activity
            )
        )
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
        statusBarView.id =
            FAKE_TRANSLUCENT_VIEW_ID
        return statusBarView
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private fun calculateStatusColor(
        @ColorInt color: Int, @IntRange(
            from = 0,
            to = 255
        ) alpha: Int
    ): Int {
        if (alpha == 0) {
            return color
        }
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }

    // =============================================================================================

    private fun setMIUILightStatusBar(activity: Activity, darkmode: Boolean): Boolean {
        val clazz = activity.window.javaClass
        try {
            var darkModeFlag = 0
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.invoke(activity.window, if (darkmode) darkModeFlag else 0, darkModeFlag)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    private fun setFlymeLightStatusBar(activity: Activity?, dark: Boolean): Boolean {
        var result = false
        if (activity != null) {
            val lp = activity.window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java
                .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java
                .getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (dark) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            activity.window.attributes = lp
            result = true
        }
        return result
    }

    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            // We want to change tint color to white again.
            // You can also record the flags in advance so that you can turn UI back completely if
            // you have set other flags before, such as translucent or full screen.
            decor.systemUiVisibility = 0
        }
    }
    //================================================================================

    var DEFAULT_COLOR = 0
    var DEFAULT_ALPHA =
        0f //Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 0.2f : 0.3f;

    const val MIN_API = 17

    //<editor-fold desc="沉侵">
    fun immersive(activity: Activity) {
        immersive(
            activity,
            DEFAULT_COLOR,
            DEFAULT_ALPHA
        )
    }

    fun immersive(
        activity: Activity, color: Int, @FloatRange(
            from = 0.0,
            to = 1.0
        ) alpha: Float
    ) {
        immersive(
            activity.window,
            color,
            alpha
        )
    }

    fun immersive(activity: Activity, color: Int) {
        immersive(
            activity.window,
            color,
            1f
        )
    }

    fun immersive(window: Window) {
        immersive(
            window,
            DEFAULT_COLOR,
            DEFAULT_ALPHA
        )
    }

    fun immersive(window: Window, color: Int) {
        immersive(
            window,
            color,
            1f
        )
    }

    fun immersive(
        window: Window, color: Int, @FloatRange(
            from = 0.0,
            to = 1.0
        ) alpha: Float
    ) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor =
                mixtureColor(
                    color,
                    alpha
                )
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = systemUiVisibility
        } else if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            setTranslucentView(
                window.decorView as ViewGroup,
                color,
                alpha
            )
        } else if (Build.VERSION.SDK_INT >= MIN_API && Build.VERSION.SDK_INT > 16) {
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = systemUiVisibility
        }
    }
    //</editor-fold>

    //</editor-fold>
//<editor-fold desc="DarkMode">
    fun darkMode(activity: Activity, dark: Boolean) {
        if (isFlyme4Later()) {
            darkModeForFlyme4(
                activity.window,
                dark
            )
        } else if (isMIUI6Later()) {
            darkModeForMIUI6(
                activity.window,
                dark
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            darkModeForM(
                activity.window,
                dark
            )
        }
    }

    /** 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)  */
    fun darkMode(activity: Activity) {
        darkMode(
            activity.window,
            DEFAULT_COLOR,
            DEFAULT_ALPHA
        )
    }

    fun darkMode(
        activity: Activity, color: Int, @FloatRange(
            from = 0.0,
            to = 1.0
        ) alpha: Float
    ) {
        darkMode(
            activity.window,
            color,
            alpha
        )
    }

    /** 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)  */
    fun darkMode(
        window: Window, color: Int, @FloatRange(
            from = 0.0,
            to = 1.0
        ) alpha: Float
    ) {
        if (isFlyme4Later()) {
            darkModeForFlyme4(
                window,
                true
            )
            immersive(
                window,
                color,
                alpha
            )
        } else if (isMIUI6Later()) {
            darkModeForMIUI6(
                window,
                true
            )
            immersive(
                window,
                color,
                alpha
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            darkModeForM(
                window,
                true
            )
            immersive(
                window,
                color,
                alpha
            )
        } else if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            setTranslucentView(
                window.decorView as ViewGroup,
                color,
                alpha
            )
        } else {
            immersive(
                window,
                color,
                alpha
            )
        }
        //        if (Build.VERSION.SDK_INT >= 21) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//        setTranslucentView((ViewGroup) window.getDecorView(), color, alpha);
    }

    //------------------------->

    //------------------------->
    /** android 6.0设置字体颜色  */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun darkModeForM(
        window: Window,
        dark: Boolean
    ) { //        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility = if (dark) {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.decorView.systemUiVisibility = systemUiVisibility
    }

    /**
     * 设置Flyme4+的darkMode,darkMode时候字体颜色及icon变黑
     * http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
     */
    fun darkModeForFlyme4(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val e = window.attributes
                val darkFlag =
                    WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags =
                    WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(e)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(e, value)
                window.attributes = e
                result = true
            } catch (var8: java.lang.Exception) {
                Log.e("StatusBar", "darkIcon: failed")
            }
        }
        return result
    }

    /**
     * 设置MIUI6+的状态栏是否为darkMode,darkMode时候字体颜色及icon变黑
     * http://dev.xiaomi.com/doc/p=4769/
     */
    fun darkModeForMIUI6(
        window: Window,
        darkmode: Boolean
    ): Boolean {
        val clazz: Class<out Window> = window.javaClass
        try {
            var darkModeFlag = 0
            val layoutParams =
                Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field =
                layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.invoke(window, if (darkmode) darkModeFlag else 0, darkModeFlag)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            darkModeForM(
                window,
                darkmode
            )
        }
        return true
    }

    /** 判断是否Flyme4以上  */
    fun isFlyme4Later(): Boolean {
        return (Build.FINGERPRINT.contains("Flyme_OS_4")
                || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                || Pattern.compile(
            "Flyme OS [4|5]",
            Pattern.CASE_INSENSITIVE
        ).matcher(Build.DISPLAY).find())
    }

    /** 判断是否为MIUI6以上  */
    fun isMIUI6Later(): Boolean {
        return try {
            val clz = Class.forName("android.os.SystemProperties")
            val mtd = clz.getMethod("get", String::class.java)
            var `val` = mtd.invoke(null, "ro.miui.ui.version.name") as String
            `val` = `val`.replace("[vV]".toRegex(), "")
            val version = `val`.toInt()
            version >= 6
        } catch (e: java.lang.Exception) {
            false
        }
    }
    //</editor-fold>


    //</editor-fold>
    /** 增加View的paddingTop,增加的值为状态栏高度  */
    fun setPadding(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            view.setPadding(
                view.paddingLeft, view.paddingTop + getStatusBarHeight(
                    context
                ),
                view.paddingRight, view.paddingBottom
            )
        }
    }

    /** 增加View的paddingTop,增加的值为状态栏高度 (智能判断，并设置高度) */
    fun setPaddingSmart(
        context: Context,
        view: View
    ) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            val lp = view.layoutParams
            if (lp != null && lp.height > 0) {
                lp.height += getStatusBarHeight(
                    context
                ) //增高
            }
            view.setPadding(
                view.paddingLeft, view.paddingTop + getStatusBarHeight(
                    context
                ),
                view.paddingRight, view.paddingBottom
            )
        }
    }

    /** 增加View的高度以及paddingTop,增加的值为状态栏高度.一般是在沉浸式全屏给ToolBar用的  */
    fun setHeightAndPadding(
        context: Context,
        view: View
    ) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            val lp = view.layoutParams
            lp.height += getStatusBarHeight(
                context
            ) //增高
            view.setPadding(
                view.paddingLeft, view.paddingTop + getStatusBarHeight(
                    context
                ),
                view.paddingRight, view.paddingBottom
            )
        }
    }

    /** 增加View上边距（MarginTop）一般是给高度为 WARP_CONTENT 的小控件用的 */
    fun setMargin(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            val lp = view.layoutParams
            if (lp is MarginLayoutParams) {
                lp.topMargin += getStatusBarHeight(
                    context
                ) //增高
            }
            view.layoutParams = lp
        }
    }

    /**
     * 创建假的透明栏
     */
    fun setTranslucentView(
        container: ViewGroup, color: Int, @FloatRange(
            from = 0.0,
            to = 1.0
        ) alpha: Float
    ) {
        if (Build.VERSION.SDK_INT >= 19) {
            val mixtureColor =
                mixtureColor(
                    color,
                    alpha
                )
            var translucentView =
                container.findViewById<View>(android.R.id.custom)
            if (translucentView == null && mixtureColor != 0) {
                translucentView = View(container.context)
                translucentView.id = android.R.id.custom
                val lp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(
                        container.context
                    )
                )
                container.addView(translucentView, lp)
            }
            translucentView?.setBackgroundColor(mixtureColor)
        }
    }

    fun mixtureColor(color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        val a = if (color and -0x1000000 == 0) 0xff else color ushr 24
        return color and 0x00ffffff or ((a * alpha).toInt() shl 24)
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 24
        val resId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        result = if (resId > 0) {
            context.resources.getDimensionPixelSize(resId)
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                result.toFloat(), Resources.getSystem().displayMetrics
            ).toInt()
        }
        return result
    }

    /**
     * 设置状态栏透明
     */
    @TargetApi(19)
    fun setTranslucentStatus(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
            val decorView = window.decorView
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            val option =
                (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
            //导航栏颜色也可以正常设置
            //window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            val attributes = window.attributes
            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            attributes.flags = attributes.flags or flagTranslucentStatus
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //attributes.flags |= flagTranslucentNavigation;
            window.attributes = attributes
        }
    }
    /**
     * 代码实现android:fitsSystemWindows
     *
     * @param activity
     */
    fun setRootViewFitsSystemWindows(activity: Activity, fitSystemWindows: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val winContent = activity.findViewById<View>(android.R.id.content) as ViewGroup
            if (winContent.childCount > 0) {
                val rootView = winContent.getChildAt(0) as ViewGroup
                if (rootView != null) {
                    rootView.fitsSystemWindows = fitSystemWindows
                }
            }
        }
    }
    /**
     * 设置状态栏深色浅色切换
     */
    fun setStatusBarDarkTheme(activity: Activity?, dark: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StatusBarHelp.setStatusBarFontIconDark(activity, StatusBarHelp.TYPE_M, dark)
            } else if (OSUtils.isMiui()) {
                StatusBarHelp.setStatusBarFontIconDark(activity, StatusBarHelp.TYPE_MIUI, dark)
            } else if (OSUtils.isFlyme()) {
                StatusBarHelp.setStatusBarFontIconDark(activity, StatusBarHelp.TYPE_FLYME, dark)
            } else { //其他情况
                return false
            }
            return true
        }
        return false
    }



}