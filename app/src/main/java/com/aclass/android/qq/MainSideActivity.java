package com.aclass.android.qq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.aclass.android.qq.common.MyButtonOperation;

public class MainSideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_side);
//        Button vipbtn=(Button)findViewById(R.id.vipBtn);
//        MyButtonOperation.changeButtonBG(this,vipbtn,R.drawable.main_side_btn_down,R.drawable.main_side_btn_up);
    }
}
