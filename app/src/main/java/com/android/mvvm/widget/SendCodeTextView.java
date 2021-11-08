package com.android.mvvm.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;


import com.android.mvvm.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: Lance
 * @description: 发送短信验证码倒计时的textview
 * @date: 2020/3/3 17:30
 * @version: 1.0
 */
public class SendCodeTextView extends AppCompatTextView {

    private static final int DEFAULT_TIME = 120;
    private int resendTime = DEFAULT_TIME;//重新发送短信的时间
    private int second = resendTime; //倒计时秒数
    private String showText = "获取验证码";//显示的内容

    TimerTask timerTask;
    Timer timer;

    @SuppressLint({"HandlerLeak", "SetTextI18n"})
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (second < 0) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    timerTask.cancel();
                    timerTask = null;
                    second = resendTime;
                }
                setText(showText);
                setEnabled(true);
                setBackgroundResource(R.drawable.bg_code_cyan);
            } else {
                setText("重新获取（" + second + "s）");
                setBackgroundResource(R.drawable.bg_code_black);
                second--;
            }
        }
    };

    public SendCodeTextView(Context context) {
        super(context);
    }

    public SendCodeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        showText = getText().toString();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SendCodeTextView,
                0, 0);
        try {
            resendTime = a.getInteger(R.styleable.SendCodeTextView_resendTime, DEFAULT_TIME);
            second = resendTime;
        } finally {
            a.recycle();
        }
    }

    public SendCodeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        showText = getText().toString();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SendCodeTextView,
                0, 0);
        try {
            resendTime = a.getInteger(R.styleable.SendCodeTextView_resendTime, DEFAULT_TIME);
            second = resendTime;
        } finally {
            a.recycle();
        }
    }

    public void startTime() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
        setEnabled(false);
    }

    public void stopTime() {
        mHandler.removeCallbacks(timerTask);
        if (timer != null) {
            timer.cancel();
            timer = null;
            timerTask.cancel();
            timerTask = null;
            second = resendTime;
        }
        second = 0;
    }

    /**
     * 设置发送短信时间间隔
     */
    public void setResendTime(int resendTime) {
        this.resendTime = resendTime;
    }
}
