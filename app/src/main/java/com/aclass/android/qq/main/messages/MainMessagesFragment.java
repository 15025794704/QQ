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
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private LinearLayout msgListBox;
    private MainActivity mActivity;

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

    protected void readFile(){
        try {
            FileInputStream fis = mActivity.openFileInput("messageList.json");
            if(fis==null)
                return;

            //读取json数据
            byte[] data = new byte[fis.available()];
            fis.read(data);
            String json=new String(data,0,data.length);
            json="["+json.substring(0,json.length()-1)+"]";
            //转换json数据
            Attribute.msgList=new ArrayList<>();
            Gson gson = new Gson();
            Type listType=new TypeToken<List<MsgList>>(){}.getType();
            Attribute. msgList = gson.fromJson(json, listType);

            //加载
            msgListBox.removeAllViewsInLayout();
            for (int i=0;i<Attribute.msgList.size();i++ ) {
                MsgList msg =Attribute. msgList.get(i);
                View view=fillValue(msg);
                msgListBox.addView(view,i);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private View fillValue(final MsgList msgList){
        View view= View.inflate(mActivity, R.layout.messages_list_layout, null);
        final LinearLayout LinearMsgC=(LinearLayout) view.findViewById(R.id.LinearMsgC);
        LinearLayout LinearMsgP=(LinearLayout) view.findViewById(R.id.LinearMsgP);
        RoundImageView HeadMsg=(RoundImageView) view.findViewById(R.id.HeadMsg);
        TextView NameMsg=(TextView) view.findViewById(R.id.NameMsg);
        TextView TimeMsg=(TextView) view.findViewById(R.id.TimeMsg);
        final Button btnTop=(Button) view.findViewById(R.id.btnTop);
        Button btnDelete=(Button) view.findViewById(R.id.btnDelete);

        NameMsg.setText( msgList.getQQFriend());
        TimeMsg.setText(msgList.getTime());
        if(msgList.isTop()) {
            btnTop.setText("取消置顶");
            LinearMsgC.setBackgroundColor(Color.parseColor("#eee"));
        }
        else{
            btnTop.setText("置顶");
            LinearMsgC.setBackgroundColor(Color.parseColor("#fff"));
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
                                    LinearMsgC.setBackgroundColor(Color.parseColor("#eee"));
                                    msg.setTop(true);
                                }
                                else {
                                    LinearMsgC.setBackgroundColor(Color.parseColor("#fff"));
                                    msg.setTop(false);
                                }
                                msg.setIndex(mc + 1);
                                Attribute.msgList.remove(i);
                                Attribute.msgList.add(mc + 1, msg);
                                Receiver.writeMsgListToFile(mActivity);
                                msgListBox.removeViewAt(i);
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
        Receiver.writeMsgListToFile(mActivity);
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


