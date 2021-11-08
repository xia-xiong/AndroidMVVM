package com.android.libsBase.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import com.android.libsBase.R;


public class ProgressView extends Dialog {

    public ProgressView(Context context) {
        this(context, R.style.CustomDialog);
    }

    public ProgressView(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context) {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.loading);//loading的xml文件
        findViewById(R.id.pb_load).setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.progress_loading));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {//开启
        if (findViewById(R.id.pb_load) != null && findViewById(R.id.pb_load).getAnimation() == null) {
            findViewById(R.id.pb_load).post(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.pb_load).setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.progress_loading));
                }
            });
        }
        super.show();
    }

    @Override
    public void dismiss() {//关闭
        super.dismiss();
    }
}