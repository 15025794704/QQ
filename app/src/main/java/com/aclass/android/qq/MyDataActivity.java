package com.aclass.android.qq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import com.aclass.android.qq.common.MyButtonOperation;


/**
 * Created by Adminstrator on 2019/11/19.
 */

public class MyDataActivity extends AppCompatActivity {
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
    }
}
