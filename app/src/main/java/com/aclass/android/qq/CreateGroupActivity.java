package com.aclass.android.qq;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aclass.android.qq.custom.GeneralActivity;

public class CreateGroupActivity extends GeneralActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        init();
    }

    private void init(){
        Toolbar toolbar=(Toolbar) findViewById(R.id.activity_create_group_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void consumeInsets(Rect insets) {
        findViewById(R.id.activity_create_group_toolbar).setPadding(0,insets.top,0,0);
    }
}
