package com.aclass.android.qq;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginWindowActivity extends AppCompatActivity {
    Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_login);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        registerButton= (Button) findViewById(R.id.register);
        //跳转到注册界面
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent=new Intent(LoginWindowActivity.this,RegisterWindowActivity.class);
                startActivity(registerIntent);
            }
        });

    }
}
