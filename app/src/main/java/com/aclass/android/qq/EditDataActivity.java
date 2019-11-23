package com.aclass.android.qq;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.tools.MyDateBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EditDataActivity extends AppCompatActivity {

    private ImageView mChooes;
    private TextView mQianMing,mNiCheng,mSex,mBrith,mZhiYe,mAddress,mHomeAddress,mDescript;
    private EditText mGongSi,mEmail;
    private RecyclerView mRvSchool1;

    MyDateBase myDateBase =new MyDateBase();
    User user= myDateBase.getUser("1234567890");
    List<String> teachlist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
    }
    private void init(){
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
        Bitmap headImage = myDateBase.getImageByQQ(user.getQQNum());

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

        if (!(user.getSchool()==null)||!(user.getSchool().isEmpty())){
            teachlist.add(user.getSchool());
        }
        mRvSchool1.setLayoutManager(new LinearLayoutManager(EditDataActivity.this));
        mRvSchool1.setAdapter(new LinearAdapter(EditDataActivity.this,teachlist));
    }
}

