package com.aclass.android.qq;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.common.MyButtonOperation;
import com.aclass.android.qq.custom.control.RoundImageView;

public class VideoWindowActivity extends AppCompatActivity {

    ImageButton btn_refuse;
    ImageButton btn_mic;
    ImageButton btn_loudspeaker;
    ImageButton btn_mini;

    RoundImageView headImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_video);
        init();
       set_btn_mini_click();
       set_btn_refuse_click();
    }

    //最小化按钮执行方法
    protected void set_btn_mini_click(){
        btn_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //d挂断按钮执行方法
    protected void set_btn_refuse_click(){
        btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送断开请求
                finish();
            }
        });
    }

    protected void init(){
        //设置状态栏背景
      //  ActivityOpreation.setStatusBar(this,R.color.colorVideoViewBG,View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        btn_refuse=(ImageButton)findViewById(R.id.image_btn_refuse);
        btn_mic=(ImageButton)findViewById(R.id.image_btn_mic);
        btn_loudspeaker=(ImageButton)findViewById(R.id.image_btn_loudspeaker);
        btn_mini=(ImageButton)findViewById(R.id.image_btn_mini);
        headImg=(RoundImageView)findViewById(R.id.RoundImageView_video_head);

        //设置拒绝按钮按下和弹起效果
        MyButtonOperation.changeImageButton(this,btn_refuse,R.drawable.btn_refuse_2,R.drawable.btn_refuse_1);
        //设置小化按钮按下和弹起效果
        MyButtonOperation.changeImageButton(this,btn_mini,R.drawable.videoview_mini_btn2,R.drawable.videoview_mini_btn);
    }

}
