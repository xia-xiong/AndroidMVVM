package com.android.libsBase.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.libsBase.R;

import org.jetbrains.annotations.NotNull;

/**
 *loading
 */
public class CommLoadingDialog extends Dialog {
    public CommLoadingDialog(@NonNull Context context) {
        super(context,R.style.dialog);
    }
    private  TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setCanceledOnTouchOutside(false);
        @SuppressLint("InflateParams") View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading_view, null);
        titleView= rootView.findViewById(R.id.title);
        setContentView(rootView);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void setMessage(@NotNull String mContent) {
        if(titleView!=null) {
            titleView.setText(mContent);
        }
    }
}
