package com.aclass.android.qq;

import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.aclass.android.qq.custom.GeneralActivity;

public class SplashActivity extends GeneralActivity {
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_window);
        // 隐藏状态栏
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_FULLSCREEN);
        imageView = (ImageView) findViewById(R.id.img_show);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        },2500);
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
