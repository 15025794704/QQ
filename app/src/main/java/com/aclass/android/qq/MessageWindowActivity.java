package com.aclass.android.qq;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.common.MyBitMapOperation;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.databinding.ActivityWindowMessageBinding;
import com.aclass.android.qq.entity.Message;
import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MessageWindowActivity extends GeneralActivity implements Toolbar.OnMenuItemClickListener {
    // DataBinding 对象
    private ActivityWindowMessageBinding mViews;
    private Thread threadStartVideo;
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.messageToolbarCall: // 语音通话
                ActivityOpreation.jumpActivity(this,VideoWindowActivity.class,new String[]{"send","1234567890"});
                return true;
            case R.id.messageToolbarInfo: // 聊天详情
                return true;
        }
        return true;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            if (msg.what == 0x11) {
            }
            else if(msg.what==0x12){
                Toast.makeText(MessageWindowActivity.this,bundle.getString("msg"),Toast.LENGTH_LONG).show();
            }
            else if(msg.what == 0x13){
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_message);
        init();
        startThreadStartVideo();
    }

    @Override
    protected void consumeInsets(Rect insets) {
        ((LinearLayout)findViewById(R.id.LinearLayout_message_window)).setPadding(0,insets.top,0,0);
    }

    protected void init(){
        // DataBinding，摆脱 findViewById
        mViews = ActivityWindowMessageBinding.inflate(getLayoutInflater());
        // 设置页面界面
        setContentView(mViews.getRoot());
        Toolbar toolbar = mViews.messageToolbar;
        // 工具栏选项
        toolbar.inflateMenu(R.menu.toolbar_message);
        // 工具栏选项点击监听器
        toolbar.setOnMenuItemClickListener(this);
        // 设置工具栏导航图标
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_24);

        toolbar.getNavigationIcon().setTint(Color.WHITE);
        for(int i=0;i<toolbar.getMenu().size();i++)
            toolbar.getMenu().getItem(i).getIcon().setTint(Color.WHITE);
        // 设置工具栏导航按键事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 设置工具栏标题文字
        mViews.messageToolbarTitle.setText("吴小吴");
    }


    private void startThreadStartVideo(){

        threadStartVideo=new Thread(new Runnable() {//发送服务器登录信息
            @Override
            public void run() {
                try {
                    DatagramSocket receiveSocket = new DatagramSocket();
                    byte[] buf;
                    User user = new User();
                    user.setQQNum("1234567890");
                    Request request = new Request(0,"",user);
                    buf=MyDateBase.toByteArray(request);
                    receiveSocket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName("47.107.138.4"),890));
                    DatagramPacket dpReceive;

                    while (true) {
                        buf = new byte[1024*10];
                        dpReceive = new DatagramPacket(buf, buf.length);
//                        lock.acquire();
                        receiveSocket.receive(dpReceive);
                        request = (Request) MyDateBase.toObject(buf, dpReceive.getLength());
                        if (request.getRequestType() == 8) {
                            String send=( (Message)request.getObj()).getSendQQ();
                            ActivityOpreation.jumpActivity(MessageWindowActivity.this,VideoWindowActivity.class,new String[]{"receive",send});
                            Attribute.friendVideoRequest=request;
                        }
//                        lock.release();
                    }
                }
                catch (Exception e){
                    ActivityOpreation.updateUI(handler, 0x12, "开启端口失败");
                }
            }
        });

        threadStartVideo.start();
    }
}
