package com.android.libs_common.base;

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
import com.android.libs_common.utils.StringUtils;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class CommLoadingDialog extends Dialog {
    private String title;
    public CommLoadingDialog(@NonNull Context context) {
        super(context,R.style.dialog);
    }
    private  TextView titleView;

    private LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setCanceledOnTouchOutside(false);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading_view, null);
        titleView= rootView.findViewById(R.id.title);
        setContentView(rootView);
//        animationView = findViewById(R.id.animation_view);
//        animationView.setAnimation("shuaxin_white.json");
//        animationView.loop(true);
//        animationView.playAnimation();
    }

    @Override
    public void dismiss() {
        super.dismiss();
//        animationView.cancelAnimation();
    }

    public void setMessage(@NotNull String mContent) {
        if(StringUtils.isNotBlank(title)&&titleView!=null) {
            titleView.setText(mContent);
        }
    }
}
