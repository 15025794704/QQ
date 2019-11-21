package com.aclass.android.qq;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aclass.android.qq.common.GraphicsUtil;
import com.aclass.android.qq.common.MyButtonOperation;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;

import org.w3c.dom.Text;


/**
 * Created by Adminstrator on 2019/11/19.
 */

public class MyDataActivity extends GeneralActivity {
    private Button idBtn,editBtn,sendBtn;
    private ImageView head;
    private TextView name,qq,sex,xingZuo,qianMing,myReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_my);

        Button idBtn=(Button)findViewById(R.id.idBtn);
        MyButtonOperation.changeButtonBG(this,idBtn,R.drawable.main_side_btn_down,R.drawable.main_side_btn_up);

        Button editBtn=(Button)findViewById(R.id.editBtn);
        MyButtonOperation.changeButtonBG(this,editBtn,R.drawable.main_side_btn_down,R.drawable.main_side_btn_up);

        Button sendBtn=(Button)findViewById(R.id.sendBtn);
        MyButtonOperation.changeButtonBG(this,sendBtn,R.drawable.main_side_btn_down,R.drawable.main_side_btn_up);
        init();
    }

    @Override
    protected void consumeInsets(Rect insets) {
        ((RelativeLayout)findViewById(R.id.myBackground)).setPadding(0,insets.top,0,0);
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

        myReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //加数据
        Intent intent=getIntent();
        String qqNum=intent.getStringExtra("qqNum");
        User user= Attribute.userInfoList.get(qqNum);
        if(user!=null){
            Bitmap bitmap=Attribute.userHeadList.get(qqNum);
               if( bitmap!=null) {
                   bitmap = GraphicsUtil.round(bitmap);
                   head.setImageBitmap(bitmap);
               }
                else{
                   head.setImageDrawable(getDrawable(R.drawable.main_side_icon));
               }
            name.setText(user.getNiCheng());
            qq.setText(user.getQQNum());
            if( user.getSex()!=null &&( user.getSex().equals("男") || user.getSex().equals("女"))){
                sex.setText(user.getSex());
            }
            else
                sex.setText("男");

            if(user.getXingZuo()!=null &&!(user.getXingZuo().equalsIgnoreCase("null") || user.getXingZuo().equalsIgnoreCase(""))){
                xingZuo.setText(user.getXingZuo());
            }
            else
                xingZuo.setText("白羊座");

            if(user.getQianMing()!=null &&!(user.getQianMing().equalsIgnoreCase("null") || user.getQianMing().equalsIgnoreCase(""))){
                qianMing.setText(user.getQianMing());
            }
            else
                qianMing.setText("编辑签名，展示我的独特态度");
        }
    }
}
