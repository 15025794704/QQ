package com.aclass.android.qq;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aclass.android.qq.chat.contact.account.ContactAccountSettingsActivity;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.common.GraphicsUtil;
import com.aclass.android.qq.common.MyButtonOperation;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;


/**
 * Created by Adminstrator on 2019/11/19.
 */

public class MyDataActivity extends GeneralActivity {
    private Button idBtn,editBtn,sendBtn;
    private ImageView head;
    private TextView name,qq,sex,xingZuo,qianMing,myReturn;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_my);
        init();
    }

    @Override
    protected void consumeInsets(Rect insets) {
        ((RelativeLayout)findViewById(R.id.myBackground)).setPadding(0,insets.top,0,0);
        RadioGroup bottomOptions = findViewById(R.id.bottomNavigation);
        bottomOptions.setPadding(
                bottomOptions.getPaddingStart(),
                bottomOptions.getPaddingTop(),
                bottomOptions.getPaddingEnd(),
                bottomOptions.getPaddingBottom() + insets.bottom
        );
    }

    private void init(){
         idBtn=(Button)findViewById(R.id.idBtn);
        MyButtonOperation.changeButtonBG(this,idBtn,R.drawable.main_side_btn_down,R.drawable.main_side_btn_up);

         editBtn=(Button)findViewById(R.id.editBtn);
        MyButtonOperation.changeButtonBG(this,editBtn,R.drawable.main_side_btn_down,R.drawable.main_side_btn_up);

         sendBtn=(Button)findViewById(R.id.sendBtn);
        MyButtonOperation.changeButtonBG(this,sendBtn,R.drawable.main_side_btn_down,R.drawable.main_side_btn_up);

        head=(ImageView)findViewById(R.id.myIcon);
        name=(TextView)findViewById(R.id.myName);
        qq=(TextView)findViewById(R.id.qqNum);
        sex=(TextView)findViewById(R.id.sex);
        xingZuo=(TextView)findViewById(R.id.constellation);
        qianMing=(TextView)findViewById(R.id.myDetail);
        myReturn=(TextView)findViewById(R.id.myReturn);


        //加数据
        Intent intent=getIntent();
        final String qqNum=intent.getStringExtra("qqNum");
        mUser = Attribute.userInfoList.get(qqNum);
        // 非好友
        if (mUser == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyDateBase dateBase = new MyDateBase();
                    mUser = dateBase.getUser(qqNum);
                    dateBase.Destory();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initWithUser();
                        }
                    });
                }
            }).start();
        } else {
            initWithUser();
        }
    }

    private void initWithUser(){
        if (mUser == null) return;

        if(!Attribute.QQ.equals(mUser.getQQNum()))
            editBtn.setText("好友设置");
        else
            editBtn.setText("编辑资料");

        Bitmap bitmap=Attribute.userHeadList.get(mUser.getQQNum());
        if( bitmap!=null) {
            bitmap = GraphicsUtil.round(bitmap);
            head.setImageBitmap(bitmap);
        }
        else{
            head.setImageDrawable(getDrawable(R.drawable.main_side_icon));
        }
        name.setText(mUser.getNiCheng());
        qq.setText(mUser.getQQNum());
        if( mUser.getSex()!=null &&( mUser.getSex().equals("男") || mUser.getSex().equals("女"))){
            sex.setText(mUser.getSex());
        }
        else
            sex.setText("男");

        if(mUser.getXingZuo()!=null &&!(mUser.getXingZuo().equalsIgnoreCase("null") || mUser.getXingZuo().equalsIgnoreCase(""))){
            xingZuo.setText(mUser.getXingZuo());
        }
        else
            xingZuo.setText("白羊座");

        if(mUser.getQianMing()!=null &&!(mUser.getQianMing().equalsIgnoreCase("null") || mUser.getQianMing().equalsIgnoreCase(""))){
            qianMing.setText(mUser.getQianMing());
        }
        else
            qianMing.setText("编辑签名，展示我的独特态度");

        setClick(this);
    }

    private void setClick(final Activity context) {
        final String number = mUser.getQQNum();
        final boolean isSelf = number.equals(Attribute.QQ);
        myReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelf) return;
                ActivityOpreation.jumpActivity(context, MessageWindowActivity.class, new String[]{ number });
                finish();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelf) {
                    ActivityOpreation.jumpActivity(context, EditDataActivity.class);
                } else {
                    Intent intent = new Intent(context, ContactAccountSettingsActivity.class);
                    intent.putExtra(ContactAccountSettingsActivity.ARG_NUM, number);
                    startActivity(intent);
                }
            }
        });
    }
}
