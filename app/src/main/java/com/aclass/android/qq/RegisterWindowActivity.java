package com.aclass.android.qq;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.tools.MyDateBase;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterWindowActivity extends GeneralActivity {
    EditText new_account;
    EditText new_pwd;
    EditText again_pwd;
    EditText nickname;
    RadioGroup sex_group;
    RadioButton male;
    RadioButton female;
    Button register_commit;
    String choose_sex=null;
    private int recLen = 3;//倒计时提示5秒
    Timer timer = new Timer();

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_register);

          operation();

        register_commit=findViewById(R.id.register);
        register_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEdit())
                {
                    final String account=new_account.getText().toString().trim();
                    final String pwd=new_pwd.getText().toString().trim();
                    final String nickName=nickname.getText().toString().trim();
                    final String sex=choose_sex;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MyDateBase myDateBase=new MyDateBase();

                            User user=new User();
                            user.setQQNum(account);
                            user.setPassword(pwd);
                            user.setNiCheng(nickName);
                            user.setSex(sex);
                            if(!myDateBase.isExitqqNum(account)) {
                                myDateBase.insertEntity(user);
                                timer.schedule(getTask(), 1000, 1000);//等待时间一秒，停顿时间一秒


                                /**
                                 * 正常情况下不点击跳过
                                 */
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ActivityOpreation.jumpActivity(RegisterWindowActivity.this,LoginWindowActivity.class);
                                        finish();
                                    }
                                }, 5000);//延迟5S后发送handler信息
                            }
                            else
                            {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterWindowActivity.this,"该qq账号已存在",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });
    }

    @Override
    protected void consumeInsets(Rect insets) {
    }

    private TimerTask getTask()
    {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() { // UI thread
                    @Override
                    public void run() {
                        recLen--;
                        Toast.makeText(RegisterWindowActivity.this,"还剩"+recLen+"秒到登录界面。。",Toast.LENGTH_SHORT).show();
                        if (recLen < 2) {
                            timer.cancel();
                        }
                    }
                });
            }
        };
        return task;

    }

    private boolean checkEdit()
    {
        if(new_account.getText().toString().trim().equals(""))
        {
            Toast.makeText(RegisterWindowActivity.this,"qq账号不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(new_pwd.getText().toString().trim().equals(""))
        {
            Toast.makeText(RegisterWindowActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(nickname.getText().toString().trim().equals(""))
        {
            Toast.makeText(RegisterWindowActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(!new_pwd.getText().toString().trim().equals(again_pwd.getText().toString().trim()))
        {
            Toast.makeText(RegisterWindowActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
        }
        else
        {
            return  true;
        }
        return false;
    }
    private void operation()
    {

        new_account=findViewById(R.id.num);
        new_pwd=findViewById(R.id.pwd);
        again_pwd=findViewById(R.id.again_pwd);
        nickname=findViewById(R.id.nickname);
        sex_group=findViewById(R.id.sexRadioGroup);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);

        //确定性别
        sex_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(RegisterWindowActivity.this.male.getId()==checkedId)
                {
                    choose_sex=RegisterWindowActivity.this.male.getText().toString().trim();
                }
                else if(RegisterWindowActivity.this.female.getId()==checkedId)
                {
                    choose_sex=RegisterWindowActivity.this.female.getText().toString().trim();
                }

            }
        });

        new_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    if(new_account.getText().toString().trim().length()!=10)
                    {
                        Toast.makeText(RegisterWindowActivity.this,"qq账号不是10个字符",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        new_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    if(new_pwd.getText().toString().trim().length()<8||new_pwd.getText().toString().trim().length()>18)
                    {
                        Toast.makeText(RegisterWindowActivity.this,"新密码的长度小于了8位或者超出了18位",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        again_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    if(again_pwd.getText().toString().trim().length()<8||again_pwd.getText().toString().trim().length()>18)
                    {
                        Toast.makeText(RegisterWindowActivity.this,"确认密码的长度小于了8位或者超出了18位",Toast.LENGTH_SHORT).show();
                    }

                    else if(!new_pwd.getText().toString().trim().equals(again_pwd.getText().toString().trim()))
                    {
                        Toast.makeText(RegisterWindowActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    if(nickname.length()>18)
                    {
                        Toast.makeText(RegisterWindowActivity.this,"昵称的长度超出了18位",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
