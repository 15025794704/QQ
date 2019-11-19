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
import android.widget.TextView;

import com.aclass.android.qq.R;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.main.MainFragment;
import com.aclass.android.qq.seek.SeekActivity;

import java.lang.reflect.InvocationTargetException;

/**
 * 应用“消息”页面
 * 聊天消息列表
 */
public class MainMessagesFragment extends Fragment implements MainFragment.MainPage, Toolbar.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {

    private MyToolbar mainToolbar;

    public static MainMessagesFragment newInstance(){
        return new MainMessagesFragment();
    }
    
    public MainMessagesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_main_messages, container, false);
        init();
        return  view;
    }

    private  void init(){
        loadMsgList();
    }

    private void loadMsgList(){

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
