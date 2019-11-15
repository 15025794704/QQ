package com.aclass.android.qq;

import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;

import com.aclass.android.qq.custom.GeneralActivity;

public class SplashActivity extends GeneralActivity {
    private ImageView imageView;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_window);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        imageView= (ImageView) findViewById(R.id.img_show);
        animationDrawable= (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        },3000);
    }

    @Override
    protected void consumeInsets(Rect insets) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}
