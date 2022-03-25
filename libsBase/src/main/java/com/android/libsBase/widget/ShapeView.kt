package com.android.libsBase.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import com.android.libsBase.R

/**
 * 自定义shape
 */

class ShapeView @JvmOverloads constructor(
    context: Context,
    attribute: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attribute, defStyleAttr) {

    private companion object {
        val TOP_LEFT = 1
        val TOP_RIGHT = 2
        val BOTTOM_LEFT = 4
        val BOTTOM_RIGHT = 8
    }

    /**
     * shape模式
     * 矩形（rectangle）、椭圆形(oval)、线形(line)、环形(ring)
     */
    private var mShapeMode = 0
    /**
     * 填充颜色
     */
    private var mFillColor = 0
    /**
     * 按压颜色
     */
    private var mPressedColor = 0
    /**
     * 描边颜色
     */
    private var mStrokeColor = 0
    /**
     * 描边宽度
     */
    private var mStrokeWidth = 0
    /**
     * 圆角半径
     */
    private var mCornerRadius = 0
    /**
     * 左上角圆角半径
     */
    private var mTopLeftCornerRadius = 0
    /**
     * 圆角位置
     */
    private var mCornerPosition = 0
    /**
     * 点击动效
     */
    private var mActiveEnabled = false
    /**
     * 起始颜色
     */
    private var mStartColor = 0
    /**
     * 结束颜色
     */
    private var mEndColor = 0
    /**
     * 渐变方向
     * 0-GradientDrawable.Orientation.TOP_BOTTOM
     * 1-GradientDrawable.Orientation.LEFT_RIGHT
     */
    private var mOrientation = 0
    /**
     * drawable位置
     * -1-null、0-left、1-top、2-right、3-bottom
     */
    private var mDrawablePosition = 0
    /**
     * 是否居中
     */
    private var mIsCenter = false
    /**
     * 普通shape样式
     */
    private val normalGradientDrawable: GradientDrawable by lazy { GradientDrawable() }
    /**
     * 按压shape样式
     */
    private val pressedGradientDrawable: GradientDrawable by lazy { GradientDrawable() }
    /**
     * shape样式集合
     */
    private val stateListDrawable: StateListDrawable by lazy { StateListDrawable() }
    // button内容总宽度
    private var contentWidth = 0f
    // button内容总高度
    private var contentHeight = 0f
    private var mGravity=0
    init {
        context.obtainStyledAttributes(attribute, R.styleable.ShapeView).apply {
            mShapeMode = getInt(R.styleable.ShapeView_sv_shapeMode, 0)
            mFillColor = getColor(R.styleable.ShapeView_sv_fillColor, 0xFFFFFFFF.toInt())
            mPressedColor =
                getColor(R.styleable.ShapeView_sv_pressedColor, 0xFF666666.toInt())
            mStrokeColor = getColor(R.styleable.ShapeView_sv_strokeColor, 0)
            mStrokeWidth = getDimensionPixelSize(R.styleable.ShapeView_sv_strokeWidth, 0)
            mCornerRadius = getDimensionPixelSize(R.styleable.ShapeView_sv_cornerRadius, 0)
            mTopLeftCornerRadius =
                getDimensionPixelSize(R.styleable.ShapeView_sv_top_left_cornerRadius, 0)
            mCornerPosition = getInt(R.styleable.ShapeView_sv_cornerPosition, -1)
            mActiveEnabled = getBoolean(R.styleable.ShapeView_sv_activeEnabled, false)
            mDrawablePosition = getInt(R.styleable.ShapeView_sv_drawablePosition, -1)
            mStartColor = getColor(R.styleable.ShapeView_sv_startColor, 0xFFFFFFFF.toInt())
            mEndColor = getColor(R.styleable.ShapeView_sv_endColor, 0xFFFFFFFF.toInt())
            mOrientation = getColor(R.styleable.ShapeView_sv_orientation, 0)
            mIsCenter = getBoolean(R.styleable.ShapeView_sv_is_center, false)
            mGravity = getInt(R.styleable.ShapeView_sv_gravity, 0)
            recycle()
        }
    }

    @SuppressLint("NewApi", "DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 初始化normal状态
        with(normalGradientDrawable) {
            //渐变色
            if (mStartColor != 0xFFFFFFFF.toInt() && mEndColor != 0xFFFFFFFF.toInt()) {
                colors = intArrayOf(mStartColor, mEndColor)
                when (mOrientation) {
                    0 -> orientation = GradientDrawable.Orientation.TOP_BOTTOM
                    1 -> orientation = GradientDrawable.Orientation.LEFT_RIGHT
                }
            }
            //填充色
            else {
                setColor(mFillColor)
            }
            when (mShapeMode) {
                0 -> shape = GradientDrawable.RECTANGLE
                1 -> shape = GradientDrawable.OVAL
                2 -> shape = GradientDrawable.LINE
                3 -> shape = GradientDrawable.RING
            }
            // 统一设置圆角半径
            if (mCornerPosition == -1) {
                cornerRadius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_PX,
                    mCornerRadius.toFloat(),
                    resources.displayMetrics
                )
            }
            //根据圆角位置设置圆角半径
            else {
                cornerRadii = getCornerRadiusByPosition()
            }
            // 默认的透明边框不绘制,否则会导致没有阴影
            if (mStrokeColor != 0) {
                setStroke(mStrokeWidth, mStrokeColor)
            }
        }
        // 是否开启点击动效
        background = if (mActiveEnabled) {
            RippleDrawable(ColorStateList.valueOf(mPressedColor), normalGradientDrawable, null)
        } else {
            normalGradientDrawable
        }

    }

    @SuppressLint("NewApi")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // 如果xml中配置了drawable则设置padding让文字移动到边缘与drawable靠在一起
        // button中配置的drawable默认贴着边缘
        if (mDrawablePosition > -1) {
            compoundDrawables.let {
                val drawable: Drawable? = compoundDrawables[mDrawablePosition]
                drawable?.let {
                    // 图片间距
                    val drawablePadding = compoundDrawablePadding
                    when (mDrawablePosition) {
                        //左右drawable
                        0, 2 -> {
                            // 图片宽度
                            val drawableWidth = it.intrinsicWidth
                            // 获取文字宽度
                            val textWidth = paint.measureText(text.toString())
                            // 内容总宽度
                            contentWidth = textWidth + drawableWidth + drawablePadding + paddingLeft
                            val rightPadding = (width - contentWidth).toInt()
                            setPadding(paddingLeft, 0, rightPadding, 0)
                        }
                        // 上下drawable
                        1, 3 -> {
                            // 图片高度
                            val drawableHeight = it.intrinsicHeight
                            // 获取文字高度
                            val fm = paint.fontMetrics
                            // 单行高度
                            val singleLineHeight =
                                Math.ceil(fm.descent.toDouble() - fm.ascent.toDouble()).toFloat()
                            // 总的行间距
                            val totalLineSpaceHeight = (lineCount - 1) * lineSpacingExtra
                            val textHeight = singleLineHeight * lineCount + totalLineSpaceHeight
                            // 内容总高度
                            contentHeight = textHeight + drawableHeight + drawablePadding
                            // 图片和文字全部靠在上侧
                            val bottomPadding = (height - contentHeight).toInt()
                            setPadding(0, 0, 0, bottomPadding)
                        }
                    }
                }
            }
        }
        // 内容居中
        when (mGravity) {
            1 -> {
                gravity = Gravity.START
            }
            else ->{
                gravity = Gravity.CENTER
            }
        }
        // 可点击
//        isClickable = true
        changeTintContextWrapperToActivity()
    }

    override fun onDraw(canvas: Canvas) {
        if (mIsCenter) { // 让图片和文字居中
            when {
                contentWidth > 0 && (mDrawablePosition == 0 || mDrawablePosition == 2) -> canvas.translate(
                    (width - contentWidth) / 2,
                    0f
                )
                contentHeight > 0 && (mDrawablePosition == 1 || mDrawablePosition == 3) -> canvas.translate(
                    0f,
                    (height - contentHeight) / 2
                )
            }
        }
        super.onDraw(canvas)
    }

    /**
     * 从support23.3.0开始View中的getContext方法返回的是TintContextWrapper而不再是Activity
     * 如果使用xml注册onClick属性，就会通过反射到Activity中去找对应的方法
     * 5.0以下系统会反射到TintContextWrapper中去找对应的方法，程序直接crash
     * 所以这里需要针对5.0以下系统单独处理View中的getContext返回值
     */
    private fun changeTintContextWrapperToActivity() {
    }

    /**
     * 从context中得到真正的activity
     */
    private fun getActivity(): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    private fun getCornerRadiusByPosition(): FloatArray {
        val result = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        val cornerRadius = mCornerRadius.toFloat()
        if (containsFlag(mCornerPosition, TOP_LEFT)) {
            if (mTopLeftCornerRadius > 0) {
                result[0] = mTopLeftCornerRadius.toFloat()
                result[1] = mTopLeftCornerRadius.toFloat()
            } else {
                result[0] = cornerRadius
                result[1] = cornerRadius
            }
        }
        if (containsFlag(mCornerPosition, TOP_RIGHT)) {
            result[2] = cornerRadius
            result[3] = cornerRadius
        }
        if (containsFlag(mCornerPosition, BOTTOM_RIGHT)) {
            result[4] = cornerRadius
            result[5] = cornerRadius
        }
        if (containsFlag(mCornerPosition, BOTTOM_LEFT)) {
            result[6] = cornerRadius
            result[7] = cornerRadius
        }
        return result
    }

    /**
     * 是否包含对应flag
     */
    private fun containsFlag(flagSet: Int, flag: Int): Boolean {
        return flagSet or flag == flagSet
    }

    fun setSvFillColor(color: Int) {
        this.mFillColor = color
        requestLayout()
    }

    fun setSvStrokeColor(color: Int) {
        this.mStrokeColor = color
        requestLayout()
    }


    fun setSvRadius(radius:Int) {
        this.mCornerRadius = radius
        requestLayout()
    }


    fun setSvStartEndColor(startColor: Int,endColor: Int) {
        this.mStartColor = startColor
        this.mEndColor = endColor
        requestLayout()
    }
}