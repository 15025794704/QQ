package com.aclass.android.qq;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.seek.SeekActivity;
import com.aclass.android.qq.tools.MyDateBase;

import java.util.List;

/**
 * Created by Adminstrator on 2019/11/21.
 */

public class NewFriendsActivity extends GeneralActivity {
    private LinearLayout contain;

    public Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends_my);
        init();
    }

    private void init(){
        MyToolbar toolbar = findViewById(R.id.new_friend_item_bar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.new_friend_seek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewFriendsActivity.this, SeekActivity.class));
            }
        });

        contain=(LinearLayout)findViewById(R.id.new_friend_item_contain);


        loadList();

    }
    public void loadList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MyDateBase dateBase = new MyDateBase();
                    List<User> list = dateBase.getFriendsUser(Attribute.QQ);
                    if (list != null) {
                        for (User user : list) {
                            final User u = user;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    View view = View.inflate(NewFriendsActivity.this, R.layout.new_friend_item, null);
                                    TextView name = (TextView) view.findViewById(R.id.new_friend_item_name);
                                    TextView des = (TextView) view.findViewById(R.id.new_friend_item_des);
                                    final Button btn = (Button) view.findViewById(R.id.new_friend_item_btn);
                                    name.setText(u.getNiCheng());
                                    des.setText("我是：" + u.getNiCheng());
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        MyDateBase myDateBase = new MyDateBase();
                                                        Friend f = myDateBase.getFriend(u.getQQNum(), Attribute.QQ);
                                                        f.setIsAgree(1);
                                                        myDateBase.updateEntity(f);
                                                        f.setQQ1(Attribute.QQ);
                                                        f.setQQ2(u.getQQNum());
                                                        f.setBeiZhu("");
                                                        myDateBase.insertEntity(f);
                                                    }
                                                    catch (Exception e2){
                                                        e2.printStackTrace();
                                                        Log.e("TAG",e2.toString());
                                                    }
                                                }
                                            }).start();
                                            Attribute.agreeFriendClick=1;
                                            btn.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                    contain.addView(view);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", e.toString());
                }
            }
        }).start();


    }

    @Override
    protected void consumeInsets(Rect insets) {
        findViewById(R.id.new_friend_item_bar).setPadding(0,insets.top,0,0);
    }
}
