package com.aclass.android.qq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aclass.android.qq.common.ActivityOpreation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActivityOpreation.jumpActivity(this,VideoWindowActivity.class);
        ActivityOpreation.jumpActivity(this,MessageWindowActivity.class);
    }
}
