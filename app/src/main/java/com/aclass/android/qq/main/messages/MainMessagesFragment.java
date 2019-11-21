package com.aclass.android.qq.main.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aclass.android.qq.MessageWindowActivity;
import com.aclass.android.qq.R;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.custom.control.RoundImageView;
import com.aclass.android.qq.databinding.FragmentDrawerBinding;
import com.aclass.android.qq.entity.Message;
import com.aclass.android.qq.entity.MsgList;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.internet.Receiver;
import com.aclass.android.qq.main.MainActivity;
import com.aclass.android.qq.main.MainFragment;
import com.aclass.android.qq.seek.SeekActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用“消息”页面
 * 聊天消息列表
 */
public class MainMessagesFragment extends Fragment implements MainFragment.MainPage, Toolbar.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {

    private MyToolbar mainToolbar;
    public static   LinearLayout msgListBox;
    private static MainActivity mActivity;

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
        loadMsgList();
    }

    public static void readFile(){
        try {
            if(Attribute.msgList==null){
                Attribute.msgList=new ArrayList<>();
            }
            FileInputStream fis = mActivity.openFileInput("messageList.json");
            if(fis==null)
                return;

            //读取json数据
            byte[] data = new byte[fis.available()];
            fis.read(data);
            String json=new String(data,0,data.length,"utf-8");
            json="["+json.substring(0,json.length()-1)+"]";

            //转换json数据
            Attribute.msgList=null;
            Gson gson = new Gson();
            Type listType=new TypeToken<List<MsgList>>(){}.getType();
            List<MsgList> lists = gson.fromJson(json, listType);
            Attribute.msgList=lists;

            //加载
            msgListBox.removeAllViewsInLayout();
            for (int i=0;i<Attribute.msgList.size();i++ ) {
                MsgList msg =Attribute. msgList.get(i);
                View view=fillValue(msg);
                Log.d("TAG","加载"+msg.isTop());
                msgListBox.addView(view,i);
            }
        }
        catch (Exception e){
            Log.e("TAG",""+e.getLocalizedMessage());
        }
    }

    private static View fillValue(final MsgList msgList){
        View view= View.inflate(mActivity, R.layout.messages_list_layout, null);
        final LinearLayout LinearMsgC=(LinearLayout) view.findViewById(R.id.LinearMsgC);
        LinearLayout LinearMsgP=(LinearLayout) view.findViewById(R.id.LinearMsgP);
        RoundImageView HeadMsg=(RoundImageView) view.findViewById(R.id.HeadMsg);
        TextView NameMsg=(TextView) view.findViewById(R.id.NameMsg);
        TextView TimeMsg=(TextView) view.findViewById(R.id.TimeMsg);
        final Button btnTop=(Button) view.findViewById(R.id.btnTop);
        Button btnDelete=(Button) view.findViewById(R.id.btnDelete);

        NameMsg.setText( msgList.getName());
        TimeMsg.setText(msgList.getTime());
        if(Attribute.userHeadList!=null && Attribute.userHeadList.containsKey(msgList.getQQFriend()))
            HeadMsg.setImageBitmap(Attribute.userHeadList.get(msgList.getQQFriend()));
        if(msgList.isTop()) {
            btnTop.setText("取消置顶");
            LinearMsgC.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
        else{
            btnTop.setText("置顶");
            LinearMsgC.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        LinearMsgC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOpreation.jumpActivity(mActivity, MessageWindowActivity.class,new String[]{msgList.getQQFriend()});
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<Attribute.msgList.size();i++){
                    if(Attribute.msgList.get(i).getQQFriend().equals(msgList.getQQFriend())){
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
                                if(btnTop.getText().equals("置顶")) {
                                    msg.setTop(true);
                                    Log.d("TAG","true");
                                }
                                else if(btnTop.getText().equals("取消置顶")){
                                    msg.setTop(false);
                                    Log.d("TAG","false");
                                }
                                msg.setIndex(mc);
                                Attribute.msgList.set(i,msg);
                                Receiver.writeMsgListToFile(mActivity);
                                readFile();
                                Log.d("TAG","重新刷新"+msg.isTop());
                                break;
                            }
                    }
            }
        });
        return view;
    }



    private void loadMsgList(){
        readFile();
    }
    @Override
    public void onPageVisible(MyToolbar toolbar, TextView title) {
        readFile();
        title.setText(R.string.mainBottomNavMessages);
        mainToolbar = toolbar;
        toolbar.setOverflowIcon(toolbar.getContext().getDrawable(R.drawable.ic_add_24));
        toolbar.inflateMenu(R.menu.toolbar_main_messages);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void onVisiblyClick() {

    }

    @Override
    public boolean onVisiblyDoubleClick() {
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainToolbarMessagesMore: // more options
                Context popContext = new ContextThemeWrapper(getContext(), R.style.AppTheme_MainMorePop);
                PopupMenu pop = new PopupMenu(popContext, mainToolbar.findViewById(R.id.mainToolbarMessagesMore));
                pop.inflate(R.menu.toolbar_main_messages_more);
                try {
                    MenuBuilder.class.getMethod("setOptionalIconsVisible", boolean.class).invoke(pop.getMenu(), true);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                int color = Color.parseColor("#FF5050F0");
                Menu menu = pop.getMenu();
                for (int i = 0; i < menu.size(); i++){
                    MenuItem menuItem = menu.getItem(i);
                    Drawable icon = menuItem.getIcon();
                    if (icon == null) continue;
                    icon.setTint(color);
                }
                pop.setOnMenuItemClickListener(this);
                pop.show();
                return true;
            case R.id.mainToolbarMessagesSeek:
                startActivity(new Intent(getActivity(), SeekActivity.class));
                return true;
        }
        return true;
    }

}


