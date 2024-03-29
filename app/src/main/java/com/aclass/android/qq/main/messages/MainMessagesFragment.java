package com.aclass.android.qq.main.messages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aclass.android.qq.CreateGroupActivity;
import com.aclass.android.qq.MessageWindowActivity;
import com.aclass.android.qq.R;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.common.ProfileUtil;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.custom.control.RoundImageView;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.MsgList;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.internet.Receiver;
import com.aclass.android.qq.main.MainActivity;
import com.aclass.android.qq.main.MainFragment;
import com.aclass.android.qq.seek.SeekActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 应用“消息”页面
 * 聊天消息列表
 */
public class MainMessagesFragment extends Fragment implements MainFragment.MainPage, Toolbar.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {

    private MyToolbar mainToolbar;
    public   LinearLayout msgListBox;
    private  MainActivity mActivity;

    public static MainMessagesFragment newInstance(){
        return new MainMessagesFragment();
    }
    
    public MainMessagesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_main_messages, container, false);
        msgListBox=view.findViewById(R.id.msgListBox);
        mActivity=(MainActivity) getActivity();
        init();
        return view;
    }

    private  void init(){
        if(Attribute.userHeadList==null)
            Attribute.userHeadList=new HashMap<String, Bitmap>();
        if(Attribute.userInfoList==null)
            Attribute.userInfoList=new HashMap<String, User>();
        if(Attribute.friendList==null)
            Attribute.friendList=new HashMap<String, Friend>();
        loadMsgList();
    }

    public static  void readFile(Activity activity){
        Log.d("TAG",Attribute.QQ);
        try {
            if(!Attribute.isReadMsgListFile){
                return;
            }
            FileInputStream fis=null;
            try {
                fis = activity.openFileInput(Attribute.QQ + "messageList.json");
            }
            catch (FileNotFoundException e2){
                activity.openFileOutput(Attribute.QQ + "messageList.json",Context.MODE_PRIVATE);
                addWeQun(activity);
                e2.printStackTrace();}
            if(fis==null)
                return;

            //读取json数据
            byte[] data = new byte[fis.available()];
            fis.read(data);
            String json=new String(data,0,data.length, StandardCharsets.UTF_8);
            if(json.equals(""))
                return;
            json="["+json.substring(0,json.length()-1)+"]";
            Log.d("TAG",json);
            //转换json数据
            Attribute.msgList=new ArrayList<>();
            Gson gson = new Gson();
            Type listType=new TypeToken<List<MsgList>>(){}.getType();
            List<MsgList> lists = gson.fromJson(json, listType);
            Attribute.msgList=lists;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private  View fillValue(final MsgList msgList,int i){
        try {
            View view = View.inflate(mActivity, R.layout.messages_list_layout, null);
            final LinearLayout LinearMsgC = (LinearLayout) view.findViewById(R.id.LinearMsgC);
            LinearLayout LinearMsgP = (LinearLayout) view.findViewById(R.id.LinearMsgP);
            RoundImageView HeadMsg = (RoundImageView) view.findViewById(R.id.HeadMsg);
            TextView NameMsg = (TextView) view.findViewById(R.id.NameMsg);
            TextView TimeMsg = (TextView) view.findViewById(R.id.TimeMsg);
            final ImageView point = (ImageView) view.findViewById(R.id.point);
            final Button btnTop = (Button) view.findViewById(R.id.btnTop);
            Button btnDelete = (Button) view.findViewById(R.id.btnDelete);

            if (msgList.getQQFriend().length() != 8) {
                String bz = Attribute.friendList.get(msgList.getQQFriend()).getBeiZhu();
                if (bz != null && !bz.equals("") && !bz.equals(msgList.getName())) {
                    msgList.setName(bz);
                    Attribute.msgList.get(i).setName(bz);
                }
            }
            NameMsg.setText(msgList.getName());
            TimeMsg.setText(msgList.getTime());
            if (Attribute.userHeadList != null && Attribute.userHeadList.containsKey(msgList.getQQFriend()))
                HeadMsg.setImageBitmap(Attribute.userHeadList.get(msgList.getQQFriend()));
            else
                HeadMsg.setImageBitmap(ProfileUtil.getDefaultProfilePhoto(getContext()));
            if (msgList.isTop()) {
                btnTop.setText("取消置顶");
                LinearMsgC.setBackgroundColor(Color.parseColor("#eeeeee"));
            } else {
                btnTop.setText("置顶");
                LinearMsgC.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            if (msgList.isPoint()) {
                point.setVisibility(View.VISIBLE);
            } else {
                point.setVisibility(View.INVISIBLE);
            }

            LinearMsgC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOpreation.jumpActivity(mActivity, MessageWindowActivity.class, new String[]{msgList.getQQFriend()});
                    Receiver.setPoint(msgList.getQQFriend(), false);
                    Receiver.writeMsgListToFile(mActivity);
                    loadMsgList();
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < Attribute.msgList.size(); i++) {
                        if (Attribute.msgList.get(i).getQQFriend().equals(msgList.getQQFriend())) {
                            Attribute.msgList.remove(i);
                            Receiver.writeMsgListToFile(mActivity);
                            msgListBox.removeViewAt(i);
                            break;
                        }
                    }
                }
            });
            btnTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < Attribute.msgList.size(); i++) {
                        if (Attribute.msgList.get(i).getQQFriend().equals(msgList.getQQFriend())) {
                            int mc = Receiver.getMaxTopCount();
                            MsgList msg = Attribute.msgList.get(i);
                            if (btnTop.getText().equals("置顶")) {
                                msg.setTop(true);
                            } else if (btnTop.getText().equals("取消置顶")) {
                                msg.setTop(false);
                            }
                            msg.setIndex(mc);
                            Attribute.msgList.add(mc, msg);
                            if (mc < i)
                                Attribute.msgList.remove(i + 1);
                            else
                                Attribute.msgList.remove(i);
                            Receiver.writeMsgListToFile(mActivity);
                            loadMsgList();
                            break;
                        }
                    }
                }
            });
            return view;
        }
        catch (Exception e){}
        return null;
    }

    private void loadMsgList(){
        readFile(mActivity);
        //加载
        msgListBox.removeAllViewsInLayout();
        for (int i=0;i<Attribute.msgList.size();i++ ) {
            MsgList msg =Attribute. msgList.get(i);
            View view=fillValue(msg,i);
            if (view!=null)
                msgListBox.addView(view);
        }
    }

    @Override
    public void onPageVisible(MyToolbar toolbar, TextView title) {

        title.setText(R.string.mainBottomNavMessages);
        mainToolbar = toolbar;
        toolbar.inflateMenu(R.menu.toolbar_main_messages);
        toolbar.setOnMenuItemClickListener(this);

        if(mActivity!=null)
            init();
    }

    @Override
    public void onVisiblyClick() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainToolbarMessagesMore: // more options
                PopupMenu pop = new PopupMenu(getContext(), mainToolbar.findViewById(R.id.mainToolbarMessagesMore));
                pop.inflate(R.menu.toolbar_main_messages_more);
                try {
                    MenuBuilder.class.getMethod("setOptionalIconsVisible", boolean.class).invoke(pop.getMenu(), true);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                pop.setOnMenuItemClickListener(this);
                pop.show();
                return true;
            case R.id.mainToolbarMessagesSeek:
                startActivity(new Intent(getActivity(), SeekActivity.class));
                return true;
            case R.id.mainToolbarMessagesNewGroupChat:
                startActivity(new Intent(getActivity(), CreateGroupActivity.class));
                return true;
        }
        return false;
    }

    /**
    *添加默认群聊
     */
    public static void addWeQun(Activity activity){
        MsgList msgList=new MsgList("QQ开发小团队","10:25","12345678",Attribute.msgList.size(),false);
        Attribute.msgList.add(msgList);
        Receiver.writeMsgListToFile(activity);
    }
}


