package com.aclass.android.qq;

import android.content.Intent;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aclass.android.qq.entity.Entity;
import com.aclass.android.qq.tools.MyDateBase;

public class LoginWindowActivity extends AppCompatActivity implements View.OnClickListener {
   EditText login_account;
    EditText login_password;
    Button user_register_button;
    Button user_login_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_window_login);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        login_account= findViewById(R.id.account_editText);
        login_password= findViewById(R.id.pwd_editText);
        user_register_button =findViewById(R.id.register_button);
        user_login_button = findViewById(R.id.login_button);
        initWidget();

    }
    private void initWidget() {

        user_login_button.setOnClickListener( this);
        user_register_button.setOnClickListener( this);

        login_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    String username = login_account.getText().toString().trim();
                    System.out.print("账号:"+username);
                    if (username.length() < 2) {
                        Toast.makeText(LoginWindowActivity.this,"QQ账号不得小于2个字符",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        login_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String pwd=login_password.getText().toString().trim();
                    System.out.print("密码:"+pwd);
                    if(pwd.length()<6)
                    {
                        Toast.makeText(LoginWindowActivity.this,"密码不得小于6个字符",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login_button:
                if(checkEdit())
                {
                    login();
                }
                break;
            case R.id.register_button:
                Intent intent_rigester=new Intent(LoginWindowActivity.this,RegisterWindowActivity.class);
                startActivity(intent_rigester);
                break;
        }

    }
    //验证用户名和密码是否为空
    private boolean checkEdit()
    {
        if(login_account.getText().toString().trim().equals("")){
            Toast.makeText(LoginWindowActivity.this,"QQ账号不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(login_password.getText().toString().trim().equals("")){
            Toast.makeText(LoginWindowActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
        }
        else
            return true;
        return false;
    }
    private void login()
    {
        MyDateBase myDateBase=new MyDateBase();
        String account=login_account.getText().toString().trim();
        String pwd=login_password.getText().toString().trim();
        boolean isExitQqAccount=myDateBase.isExitqqNum(account);
        if(isExitQqAccount)
        {
            Toast.makeText(LoginWindowActivity.this,"qq账号正确",Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(LoginWindowActivity.this,"qq账号不存在",Toast.LENGTH_SHORT).show();

        }


    }
}
