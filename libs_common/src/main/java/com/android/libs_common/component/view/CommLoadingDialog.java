package com.android.libs_common.component.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.android.libs_common.R;

/**
 * 作者：created by zhangqilu on 2018/10/23 17:56
 */
public class CommLoadingDialog extends Dialog {
    private String title;
    public CommLoadingDialog(@NonNull Context context) {
        super(context,R.style.dialog);
    }

    public CommLoadingDialog(@NonNull Context context,String title) {
        super(context,R.style.dialog);
        this.title = title;
    }
    private LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setCanceledOnTouchOutside(false);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading_view, null);
        setContentView(rootView);

//        if(StringUtils.isNotBlank(title)) {
            ((TextView) (rootView.findViewById(R.id.title))).setText(title);
//        }

        animationView = findViewById(R.id.animation_view);
        animationView.setAnimation("shuaxin_white.json");
        animationView.loop(true);
        animationView.playAnimation();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animationView.cancelAnimation();
    }
}
