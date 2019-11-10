package com.aclass.android.qq;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class StartWindowActivity extends AppCompatActivity {
    private ImageView imageView;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_window);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

          animationDrawable= (AnimationDrawable) imageView.getBackground();
          animationDrawable.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView= (ImageView) findViewById(R.id.img_show);

                Intent intent=new Intent(StartWindowActivity.this,LoginWindowActivity.class);//从一个界面跳转到另外一个界面
                startActivity(intent);
                finish();

            }
        },6000);

    }
}
