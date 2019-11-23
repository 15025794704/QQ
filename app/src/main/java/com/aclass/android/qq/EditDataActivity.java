package com.aclass.android.qq;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EditDataActivity extends GeneralActivity {

    private ImageView mChooes;
    private TextView mQianMing,mNiCheng,mSex,mBrith,mZhiYe,mAddress,mHomeAddress,mDescript;
    private EditText mGongSi,mEmail;
    private RecyclerView mRvSchool1;

//    MyDateBase myDateBase =new MyDateBase();
    User user= Attribute.currentAccount;
    List<String> teachlist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        init();
    }

    @Override
    protected void consumeInsets(Rect insets) {

        findViewById(R.id.edit_Toolbar).setPadding(0,insets.top,0,0);
    }

    private void init(){
        // 设置页面界面
        Toolbar toolbar = (Toolbar)findViewById(R.id.edit_Toolbar) ;
        // 设置工具栏导航图标
        toolbar.setNavigationIcon(R.drawable.ic_navigate_back_24);
        toolbar.getNavigationIcon().setTint(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String BirthDay;
        SimpleDateFormat temp;
        mChooes=findViewById(R.id.im_chooes);
        mQianMing=findViewById(R.id.tv_qianming);
        mNiCheng=findViewById(R.id.tv_nicheng);
        mSex=findViewById(R.id.tv_sex);
        mBrith=findViewById(R.id.tv_birth);
        mZhiYe=findViewById(R.id.tv_zhiye);
        mGongSi=findViewById(R.id.et_gongsi);
        mAddress=findViewById(R.id.tv_address);
        mHomeAddress=findViewById(R.id.tv_homeAddress);
        mEmail=findViewById(R.id.et_email);
        mDescript=findViewById(R.id.tv_descript);
        mRvSchool1=findViewById(R.id.rv_school1);
//        Bitmap headImage = myDateBase.getImageByQQ(user.getQQNum());
        Bitmap headImage = Attribute.currentAccountProfilePhoto;

        mChooes.setImageBitmap(headImage);
        mQianMing.setText(user.getQianMing());
        mNiCheng.setText(user.getNiCheng());
        mSex.setText(user.getSex());
        temp=new SimpleDateFormat("yyyy-MM-dd");
        BirthDay=temp.format(user.getBirthday());
        mBrith.setText(BirthDay);
        mZhiYe.setText(user.getZhiYe());
        mGongSi.setText(user.getGongSi());
        mAddress.setText(user.getAddress());
        mHomeAddress.setText(user.getHomeAddress());
        mEmail.setText(user.getEmail());
        mDescript.setText(user.getDescript());

//        if (!(user.getSchool()==null)||!(user.getSchool().isEmpty())){
//            teachlist.add(user.getSchool());
//        }
//        mRvSchool1.setLayoutManager(new LinearLayoutManager(EditDataActivity.this));
//        mRvSchool1.setAdapter(new LinearAdapter(EditDataActivity.this,teachlist));
    }
}

