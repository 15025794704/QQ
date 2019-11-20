package com.aclass.android.qq;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.aclass.android.qq.chat.contact.ContactChatSettingsActivity;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.common.AssetsOperation;
import com.aclass.android.qq.common.DisplayUtil;
import com.aclass.android.qq.common.Screen;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.RoundImageView;
import com.aclass.android.qq.entity.Message;
import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.internet.Receiver;
import com.aclass.android.qq.tools.MyDateBase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MessageWindowActivity extends GeneralActivity implements Toolbar.OnMenuItemClickListener {
    private String QQFriend="0987654321";
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
    private ScrollView scrollListView;
    private LinearLayout bottomViewContext;
    private LinearLayout bottomEmoji;
    private LinearLayout linearListView;
    private Thread getMsgThread;
    private boolean isExit=false;

    private int load=0;
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.messageToolbarCall: // 语音通话
                ActivityOpreation.jumpActivity(this,VideoWindowActivity.class,new String[]{"send",QQFriend});
                return true;
            case R.id.messageToolbarInfo: // 聊天详情
                Intent intent=new Intent(this, ContactChatSettingsActivity.class);
                intent.putExtra(ContactChatSettingsActivity.ARG_NUM,QQFriend);
                startActivity(intent);
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
                                                                Receiver.startReceiver(this,this);
        init();
        click();
        loadEmoji();
        fillPic();
        //询问获取权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA
                    ,Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1 );
        }
        loadMessageList();
        startMsgDidplayThread();
        fullScroll();
    }
    @Override
    protected void consumeInsets(Rect insets) {
//        ((LinearLayout)findViewById(R.id.LinearLayout_message_window)).setPadding(0,insets.top,0,0);
    }

    protected  void loadMessageList(){
        try {
            FileInputStream fis = readFile(QQFriend);
            if(fis==null)
                return;

            //读取json数据
            byte[] data = new byte[fis.available()];
            fis.read(data);
            String json=new String(data,0,data.length);
            json="["+json.substring(0,json.length()-1)+"]";
            //转换json数据
            Gson gson = new Gson();
            Type listType=new TypeToken<List<Message>>(){}.getType();
            List<Message> msgList = gson.fromJson(json, listType);

            //添加历史msg
            int showIndex=0;
            if(msgList == null)
                return;
            if( msgList.size()>20) {
                showIndex=msgList.size()-20;
            }
                    for (int i=showIndex;i<msgList.size();i++ ) {
                        Message msg = msgList.get(i);
                        if (msg.getReceiveNum().equals(QQFriend))
                            addMsg(false, msg.getContext());
                        else
                            addMsg(true, msg.getContext());
                    }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected FileInputStream readFile(String QQFriend){
        try {
            FileInputStream fis = openFileInput( QQFriend + ".json");
            return fis;
        }
        catch (FileNotFoundException e){

        }
        return null;
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
                Attribute.insertQQview="";
                isExit=true;
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
        linearListView=(LinearLayout)findViewById(R.id.message_windos_list_view);
        scrollListView=(ScrollView)findViewById(R.id.message_midde_scroll);
        listBtn=new Object[][]{{micBtn,0},{imageBtn,0},{cameraBtn,0},{redBtn,0},{emojiBtn,0}};


        View view=View.inflate(MessageWindowActivity.this, R.layout.window_message_list_item_right,null);
        TextView textView=(TextView)view.findViewById(R.id.window_message_list_item_textView);
        RoundImageView head=(RoundImageView)view.findViewById(R.id.window_message_list_item_head);


        Screen screen=new Screen(this);
        int h=screen.getposHeight();
        int w=screen.getposWidth();

        edit.setMaxHeight((int)(h*26.0/121));

        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(h*48.0/121));
        bottomView.setLayoutParams(layoutParams);
        bottomView.setVisibility(View.GONE);
    }

    private void click(){
        linearListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomView.setVisibility(View.GONE);
                closeAllBtnBG();
                hideInputView();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit.getText().toString().equals(""))
                    return;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyDateBase myDateBase=null;
                        try {
                            myDateBase = new MyDateBase();
                            myDateBase.setTimeout(600);
                            Message msg = new Message();
                            msg.setSendQQ(Attribute.QQ);
                            msg.setReceiveNum(QQFriend);
                            msg.setContext(edit.getText().toString());
                            msg.setTime(new Date());
                            myDateBase.UDPsend(new Request(5, "", msg));
                            myDateBase.receiveACK();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    addMsg(false,edit.getText().toString());
                                    edit.setText("");
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            scrollListView.fullScroll(ScrollView.FOCUS_DOWN);
                                        }
                                    });
                                }
                            });
                            Receiver.writeMessageToFile(MessageWindowActivity.this,msg,QQFriend);
                        }
                        catch (Exception e){
                            ActivityOpreation.updateUI(handler,0x12,"消息未发送成功");
                            return;
                        }
                        finally {
                            myDateBase.Destory();
                            myDateBase=null;
                        }
                    }
                }).start();

            }
        });
        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bottomView.setVisibility(View.GONE);
                closeAllBtnBG();
                fullScroll();
                return false;
            }
        });
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (load == 0) {
                    edit.setMinHeight(edit.getHeight());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(btn_send.getWidth(), btn_send.getHeight());
                    layoutParams.gravity = Gravity.BOTTOM;
                    btn_send.setLayoutParams(layoutParams);
                    load=1;
                }
            }
        });
        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (load == 0) {
                    edit.setMinHeight(edit.getHeight());
                    load=1;
                }
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

    @Override
    protected void onDestroy(){
        try {
            Attribute.insertQQview="";
            isExit=true;
            getMsgThread.stop();
        }catch (Exception e){}
        super.onDestroy();
    }

    /**
     * 按返回键隐藏所有显示的
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
        int len= DisplayUtil.sp2px(MessageWindowActivity.this,24);
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

    /**
     * Scroll回到底部
     */
    private void fullScroll(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                scrollListView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    /**
     * 改变底部表情等按钮颜色
     */
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
        fullScroll();
        return rs;
    }

    /**
     * 关闭底部所有按钮颜色
     */
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

    /**
     * 表情里添加表情按钮
     * 并设置按键事件
     */
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

    /**
     * 加载表情图片到公共属性
     */
    private void loadEmoji(){
        if(Attribute.emojiList!=null)
            return;
        AssetsOperation ao=AssetsOperation.getInstance(this);
        Attribute.emojiList=new Bitmap[20][7];
        try {
            for (int i = 0; i < 140; i++) {
                Attribute.emojiList[i/7][i%7] =ao.getImage("emoji/"+i + ".gif");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取当前窗口发来消息并显示
     */
    private void startMsgDidplayThread(){
        getMsgThread= new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if(Attribute.msgArrayList==null)
                        Attribute.msgArrayList=new ArrayList<>();
                    Attribute.msgArrayList.clear();
                    Attribute.insertQQview=QQFriend;
                    while (true){
                        if(isExit)
                            return;
                        if(!Attribute.msgArrayList.isEmpty()){
                            final Message msgg=Attribute.msgArrayList.get(0);
                            if(msgg==null)
                                continue;
                            Attribute.msgArrayList.remove(0);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    addMsg(true,msgg.getContext());
                                    fullScroll();
                                }
                            });
                        }
                        SystemClock.sleep(300);
                    }
                }
                catch (Exception e){
                    return;
                }
            }
        });
        getMsgThread.start();
    }

    /**
     * 添加消息入列表
     * @param msg
     */
    private void addMsg(boolean left,String msg){
        View view;
        TextView textView;
        RoundImageView head;
        if(left==true) {
            view = View.inflate(MessageWindowActivity.this, R.layout.window_message_list_item, null);
            textView = (TextView) view.findViewById(R.id.window_message_list_item_textView);
            head = (RoundImageView) view.findViewById(R.id.window_message_list_item_head);
        }
        else{
            view = View.inflate(MessageWindowActivity.this, R.layout.window_message_list_item_right, null);
            textView = (TextView) view.findViewById(R.id.window_message_list_item_textView_right);
            head = (RoundImageView) view.findViewById(R.id.window_message_list_item_head_right);
        }
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
        linearListView.addView(view);
    }

}
