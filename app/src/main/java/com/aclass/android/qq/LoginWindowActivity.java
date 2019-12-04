package com.aclass.android.qq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.main.MainActivity;
import com.aclass.android.qq.tools.MyDateBase;

public class LoginWindowActivity extends GeneralActivity implements View.OnClickListener {
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

        login_account= findViewById(R.id.account_editText);
        login_password= findViewById(R.id.pwd_editText);
        user_register_button =findViewById(R.id.register_button);
        user_login_button = findViewById(R.id.login_button);

        initWidget();

    }

    @Override
    protected void consumeInsets(Rect insets) {
    }

    private void initWidget() {
        Attribute.msgList.clear();
        user_login_button.setOnClickListener( this);
        user_register_button.setOnClickListener( this);

        login_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    String username = login_account.getText().toString().trim();
                    System.out.print("账号:"+username);
                    if (username.length() !=10) {
                        Toast.makeText(LoginWindowActivity.this,"QQ账号应不是10个字符",Toast.LENGTH_SHORT).show();
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
                    if(pwd.length()<8||pwd.length()>18)
                    {
                        Toast.makeText(LoginWindowActivity.this,"密码的长度小于了8位或者超出了18位",Toast.LENGTH_SHORT).show();
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
           User user=myDateBase.getUser(account);
            String userPwd=user.getPassword();
            if(userPwd.equals(pwd))
            {

                //保存登录状态，在界面保存登录的账号 定义个方法 saveLoginStatus boolean 状态 , account qq账号;
                saveLoginStatus(true, account);
                //登录成功后关闭此页面进入主页
                Intent data = new Intent();
                //datad.putExtra( ); name , value ;
                data.putExtra("isLogin", true);
                //RESULT_OK为Activity系统常量，状态码为-1
                // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                setResult(RESULT_OK, data);
                //跳转到主界面，登录成功的状态传递到 主页面 中
                startActivity(new Intent(LoginWindowActivity.this, MainActivity.class));/* MainActivity.class*/
                //销毁登录界面
                finish();
            }
            else
            {
                Toast.makeText(LoginWindowActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(LoginWindowActivity.this,"此qq账号不存在",Toast.LENGTH_SHORT).show();

        }


    }

    /*
    保存登录状态和登录用户名(账号)到SharedPreferences中
     */
    private void saveLoginStatus(boolean status,String account) {
        // GeneralPrefs 表示文件名 ,系统自动存成 GeneralPrefs.xml  SharedPreferences sp=getSharedPreferences("GeneralPrefs", MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("GeneralPrefs", MODE_PRIVATE);
        //获取编辑器
        SharedPreferences.Editor editor = sp.edit();
        //存入boolean类型的登录状态
        editor.putBoolean("isLogin", status);
        //存入登录状态时的用户名
        editor.putString("loginUserName", account);
        //提交修改
        editor.apply();
    }

}
