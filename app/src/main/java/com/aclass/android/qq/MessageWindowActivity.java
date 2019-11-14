package com.aclass.android.qq;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.common.AssetsOperation;
import com.aclass.android.qq.common.DisplayUtil;
import com.aclass.android.qq.common.Screen;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyEditText;
import com.aclass.android.qq.custom.control.RoundImageView;
import com.aclass.android.qq.entity.Message;
import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MessageWindowActivity extends GeneralActivity implements Toolbar.OnMenuItemClickListener {
    private Thread threadStartVideo;
    private String QQFriend;
    private TextView titleName;
    private EditText edit;
    private Button btn_send;
    private Object[][] listBtn;
    private ImageButton emojiBtn;
    private ImageButton cameraBtn;
    private ImageButton imageBtn;
    private ImageButton micBtn;
    private ImageButton redBtn;
    private ScrollView bottomView;
    private LinearLayout bottomViewContext;
    private LinearLayout bottomEmoji;

    private ListView msgListView;
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
        click();
//        startThreadStartVideo();
        loadEmoji();
        fillPic();

        //询问获取权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA
                    ,Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1 );
        }
    }
    @Override
    protected void consumeInsets(Rect insets) {
//        ((LinearLayout)findViewById(R.id.LinearLayout_message_window)).setPadding(0,insets.top,0,0);
    }

    protected void init(){
        // 设置页面界面
        Toolbar toolbar = (Toolbar)findViewById(R.id.messageToolbar) ;
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
        titleName=(TextView)findViewById(R.id.messageToolbarTitle_name);
        titleName.setText("吴小吴");

        btn_send=(Button)findViewById(R.id.message_btn_sendmsg) ;
        edit=(EditText)findViewById(R.id.editText_message_send);
        bottomView=(ScrollView)findViewById(R.id.message_bottom_hide_scroll);
        bottomViewContext=(LinearLayout)findViewById(R.id.message_bottom_hide_emoji);
        emojiBtn=(ImageButton)findViewById(R.id.message_btn_emoji);
        imageBtn=(ImageButton)findViewById(R.id.message_btn_image);
        micBtn=(ImageButton)findViewById(R.id.message_btn_mic);
        redBtn=(ImageButton)findViewById(R.id.message_btn_red);
        cameraBtn=(ImageButton)findViewById(R.id.message_btn_camera);
        bottomEmoji=(LinearLayout)findViewById(R.id.message_bottom_hide_emoji);
        msgListView=(ListView) findViewById(R.id.message_windos_list);

        listBtn=new Object[][]{{micBtn,0},{imageBtn,0},{cameraBtn,0},{redBtn,0},{emojiBtn,0}};

        msgListView.setAdapter(new MessageAdapter());

        Screen screen=new Screen(this);
        int h=screen.getposHeight();
        int w=screen.getposWidth();

        edit.setMaxHeight((int)(h*26.0/121));

        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(h*48.0/121));
        bottomView.setLayoutParams(layoutParams);
        bottomView.setVisibility(View.GONE);
    }

    private void click(){
        msgListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bottomView.setVisibility(View.GONE);
                closeAllBtnBG();
                hideInputView();
                return true;
            }
        });
        ((ScrollView)findViewById(R.id.message_midde_scroll)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bottomView.setVisibility(View.GONE);
                closeAllBtnBG();
                hideInputView();
                return true;
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMsg(edit.getText().toString());
                edit.setText("");
            }
        });
        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bottomView.setVisibility(View.GONE);
                closeAllBtnBG();
                return false;
            }
        });
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edit.setMinHeight(edit.getHeight());
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(btn_send.getWidth(),btn_send.getHeight());
                layoutParams.gravity= Gravity.BOTTOM;
                btn_send.setLayoutParams(layoutParams);
            }
        });
        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs=changeBg(4);
                if(rs==1) {
                    bottomView.setVisibility(View.VISIBLE);
                    bottomViewContext.setVisibility(View.VISIBLE);
                }
                else
                    bottomView.setVisibility(View.GONE);
            }
        });
        redBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs=changeBg(3);
                if(rs==1) {
                    bottomView.setVisibility(View.VISIBLE);
                    bottomViewContext.setVisibility(View.GONE);
                }
                else
                    bottomView.setVisibility(View.GONE);
            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs=changeBg(2);
                if(rs==1) {
                    bottomView.setVisibility(View.VISIBLE);
                    bottomViewContext.setVisibility(View.GONE);
                }
                else
                    bottomView.setVisibility(View.GONE);
            }
        });
        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs=changeBg(0);
                if(rs==1) {
                    bottomView.setVisibility(View.VISIBLE);
                    bottomViewContext.setVisibility(View.GONE);
                }
                else
                    bottomView.setVisibility(View.GONE);
            }
        });
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rs=changeBg(1);
                if(rs==1) {
                    bottomView.setVisibility(View.VISIBLE);
                    bottomViewContext.setVisibility(View.GONE);
                }
                else
                    bottomView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        edit.clearFocus();
    }

    /**
     * 双击返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            bottomView.setVisibility(View.GONE);
            boolean rs2;
            rs2=closeAllBtnBG();
            if(rs2==true)
                return true;
            else
                finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 插入表情
     * @param bitmap
     * @param emojiStr
     */
    private void insertEmoji(TextView text,Bitmap bitmap,String emojiStr){
        int len= DisplayUtil.sp2px(MessageWindowActivity.this,17);
        SpannableString spannableString=new SpannableString(emojiStr);
        Bitmap b=Bitmap.createScaledBitmap(bitmap,len,len,true);

        ImageSpan imageSpan=new ImageSpan(MessageWindowActivity.this,b);
        spannableString.setSpan(imageSpan, 0, emojiStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.append(spannableString);
    }

    private void insertEmoji(TextView text,String emojiStr){
        int v= emojiStr.charAt(6)-'0';
        int h=Integer.parseInt(emojiStr.substring(7,emojiStr.length()));
        if(h>Attribute.emojiList.length)
            return;
        if(v>=7)
            return;
        int len= DisplayUtil.sp2px(MessageWindowActivity.this,19);
        SpannableString spannableString=new SpannableString(emojiStr);
        Bitmap b=Bitmap.createScaledBitmap(Attribute.emojiList[h][v],len,len,true);

        ImageSpan imageSpan=new ImageSpan(MessageWindowActivity.this,b);
        spannableString.setSpan(imageSpan, 0, emojiStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.append(spannableString);
    }

    /**
     * 隐藏输入法
     */
    private boolean hideInputView(){
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    private int changeBg(int index){
        hideInputView();
        int rs=0;
        for(int i=0;i<listBtn.length;i++){
            ImageButton btn=(ImageButton) listBtn[i][0];
            int click=(int) listBtn[i][1];
            if(click==1){
                btn.getDrawable().setTint(Color.rgb(136,136,136));
                listBtn[i][1]=0;
            }
            else{
                if(index==i){
                    btn.getDrawable().setTint(Color.rgb(58,182,231));
                    listBtn[i][1]=1;
                    rs=1;
                }
            }
        }
        return rs;
    }

    private boolean closeAllBtnBG(){
        boolean rs=false;
        for(int i=0;i<listBtn.length;i++){
            ImageButton btn=(ImageButton) listBtn[i][0];
            btn.getDrawable().setTint(Color.rgb(136,136,136));
            if((int)listBtn[i][1]!=0)
                rs=true;
            listBtn[i][1]=0;
        }
        return rs;
    }

    private void fillPic(){
        final Bitmap[][] bitmaps=Attribute.emojiList;
        Screen screen=new Screen(this);
        int h=screen.getposHeight();
        int w=screen.getposWidth();

        for(int i=0;i<bitmaps.length;i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(params);
            for(int j=0;j<7;j++) {
                if(bitmaps[i][j]==null) {
                    linearLayout.addView(new Space(this));
                    continue;
                }
                ImageButton btn = new ImageButton(this);
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int)(h*9.0/121));
                params.weight = 1;
                btn.setLayoutParams(params);
                btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
                btn.setBackground(null);
                btn.setImageBitmap(bitmaps[i][j]);
                final int x=i;
                final int y=j;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertEmoji(edit,bitmaps[x][y],"[emoji"+y+""+x+"]");
                    }
                });
                linearLayout.addView(btn);
            }
            bottomEmoji.addView(linearLayout);
        }
    }

    private void loadEmoji(){
        AssetsOperation ao=AssetsOperation.getInstance(this);
        Attribute.emojiList=new Bitmap[19][7];
        try {
            for (int i = 0; i < 131; i++) {
                Attribute.emojiList[i/7][i%7] =ao.getImage("emoji/"+(i+1) + ".gif");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
                            if(!Attribute.isInVideo) {
                                String send = ((Message) request.getObj()).getSendQQ();
                                ActivityOpreation.jumpActivity(MessageWindowActivity.this, VideoWindowActivity.class, new String[]{"receive", send});
                                Attribute.friendVideoRequest = request;
                            }
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

    /**
     * 添加消息入列表
     * @param msg
     */
    private void addMsg(String msg){
        View view=View.inflate(MessageWindowActivity.this, R.layout.window_message_list_item,null);
        TextView textView=(TextView)view.findViewById(R.id.window_message_list_item_textView);
        RoundImageView head=(RoundImageView)view.findViewById(R.id.window_message_list_item_head);
        textView.setText("");
        int indexStart=0;
        for(int i=0;i<msg.length();i++){
            if(msg.charAt(i)=='[') {
                if (msg.substring(i, i + 6).equals("[emoji")) {
                    for (int j = i + 6; j < i + 10; j++) {
                        if (j<msg.length() && msg.charAt(j) == ']') {
                            textView.append(msg.substring(indexStart,i));
                            insertEmoji(textView, msg.substring(i, j));
                            i = j ;
                            indexStart=i+1;
                            break;
                        }
                    }
                }
            }
        }
        if(indexStart<msg.length()){
            textView.append(msg.substring(indexStart,msg.length()));
        }
        msgListView.addFooterView(view);
    }

    /**
     * 适配器
     */
    private class MessageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return "12";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(MessageWindowActivity.this, R.layout.window_message_list_item,null);

            return view;
        }
    }
}
