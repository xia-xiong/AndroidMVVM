package com.android.mvvm.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.android.libs_common.constant.PerfConstant
import com.android.libs_common.ext.startActivity
import com.android.libs_common.utils.Preference
import com.android.libs_common.utils.SharedPreferenceUtils
import com.android.mvvm.R
import com.android.mvvm.ui.MainActivity
import com.android.mvvm.ui.adapter.GuidePageAdapter
import com.android.mvvm.widget.ScaleCircleNavigator
import com.live.common.extension.setSingleClickListener
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.lucode.hackware.magicindicator.ViewPagerHelper

/**
 * 引导页
 * @author: 夏雄
 * @date: 2021/11/4
 */
class SplashActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private var isSplashGuide: Boolean = false

    // 图片资源的数组
    private lateinit var mImagePositionArray: IntArray

    //图片资源的集合
    private lateinit var mViewList: MutableList<View>
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(PerfConstant.SPLASH_NAME, Context.MODE_PRIVATE)
    }
    private var isLogin: Boolean by Preference(PerfConstant.Perf_LOGIN_KEY, false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initData()
        initView()
        initListener()
    }


    private fun initData() {
        isSplashGuide = sharedPreferences.getBoolean(PerfConstant.Perf_SPLASH_GUIDE_KEY, false)
    }

    private fun initView() {
        if (isSplashGuide) {
            jumpToMain()
        } else {
            initViewPager()
            initMagicIndicator()
        }
    }

    private fun initListener() {
        abt_immediately_open.setSingleClickListener {
            isSplashGuide = true
            SharedPreferenceUtils.putData(sharedPreferences, PerfConstant.Perf_SPLASH_GUIDE_KEY, isSplashGuide)
            jumpToMain()
        }
    }

    private fun initViewPager() {
        vp_splash.visibility = View.VISIBLE
        mImagePositionArray = intArrayOf(
            R.mipmap.ic_splash_one,
            R.mipmap.ic_splash_two,
            R.mipmap.ic_splash_three,
            R.mipmap.ic_splash_four
        )

        val len = mImagePositionArray.size

        mViewList = mutableListOf()

        //获取一个Layout参数，设置为全屏
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        for (i in 0 until len) {
            //new ImageView并设置全屏和图片资源
            val imageView = ImageView(this)
            imageView.layoutParams = params
            imageView.setImageResource(mImagePositionArray[i])
            //将ImageView加入到集合中
            mViewList.add(imageView)
        }

        vp_splash.adapter = GuidePageAdapter(mViewList)
    }

    private fun initMagicIndicator() {
        val scaleCircleNavigator = ScaleCircleNavigator(this)
        scaleCircleNavigator.setCircleCount(mViewList.size)
        scaleCircleNavigator.setNormalCircleColor(ContextCompat.getColor(this, R.color.iron))
        scaleCircleNavigator.setSelectedCircleColor(
            ContextCompat.getColor(
                this,
                R.color.colorAccent
            )
        )
        scaleCircleNavigator.setCircleClickListener { index -> vp_splash.currentItem = index }
        mi_splash.navigator = scaleCircleNavigator
        ViewPagerHelper.bind(mi_splash, vp_splash)
        vp_splash.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                // 判断是否是最后一页，若是则显示按钮
                if (p0 == mImagePositionArray.size - 1) {
                    abt_immediately_open.visibility = View.VISIBLE;
                } else {
                    abt_immediately_open.visibility = View.GONE;
                }
            }
        })
    }

    private fun jumpToMain() {
        launch {
            delay(1500)
            if (isLogin) {
                startActivity<MainActivity>()
            } else {
                startActivity<MainActivity>()
            }
            finish()
        }
    }
}