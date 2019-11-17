package com.aclass.android.qq.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
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

import com.aclass.android.qq.R;
import com.aclass.android.qq.common.GraphicsUtil;
import com.aclass.android.qq.custom.GeneralFragment;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.FragmentMainBinding;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.main.contacts.MainContactsFragment;
import com.aclass.android.qq.main.explore.MainExploreFragment;
import com.aclass.android.qq.main.messages.MainMessagesFragment;
import com.aclass.android.qq.seek.SeekActivity;

import java.lang.reflect.InvocationTargetException;

/**
 * 应用最主要的页面
 * 此 fragment 只有顶部工具栏和底部导航栏，导航栏对三个页面进行切换：
 * 1. {@link MainMessagesFragment “消息”页面}
 * 2. {@link MainContactsFragment “联系人”页面}
 * 3. {@link MainExploreFragment “动态”页面}
 */
public class MainFragment extends GeneralFragment implements Toolbar.OnMenuItemClickListener,
        PopupMenu.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private FragmentMainBinding mViews;
    private MainActivity mActivity;
    // 消息页面的 fragment
    private MainMessagesFragment fragmentMessages;
    // 联系人页面的 fragment
    private MainContactsFragment fragmentContacts;
    // 动态页面的 fragment
    private MainExploreFragment fragmentExplore;

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViews = FragmentMainBinding.inflate(inflater, container, false);
        return mViews.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        // 点击头像切换至抽屉页面
        mViews.mainNavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.setPagerItem(0);
            }
        });
        mViews.mainToolbar.setOnMenuItemClickListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Attribute.currentAccountProfilePhoto == null) return;
                Bitmap navIcon = GraphicsUtil.round(Attribute.currentAccountProfilePhoto);
                mViews.mainNavIcon.setImageBitmap(navIcon);
            }
        }, 600);
        mViews.mainToolbar.setOverflowIcon(getContext().getDrawable(R.drawable.ic_add_24));
        mViews.mainBottomNav.setItemIconTintList(null);
        // 导航栏点击事件监听器，进行页面切换
        mViews.mainBottomNav.setOnNavigationItemSelectedListener(this);
        // 显示消息页面
        getFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, getFragmentMessages()).commit();
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar toolbar = mViews.mainToolbar;
        toolbar.setPadding(toolbar.getPaddingStart(), insets.top, toolbar.getPaddingEnd(), toolbar.getPaddingBottom());
        BottomNavigationView bottomNav= mViews.mainBottomNav;
        bottomNav.setPadding(bottomNav.getPaddingStart(), bottomNav.getPaddingTop(), bottomNav.getPaddingEnd(), insets.bottom);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainToolbarMessagesMore: // more options
                Context popContext = new ContextThemeWrapper(getContext(), R.style.AppTheme_MainMorePop);
                PopupMenu pop = new PopupMenu(popContext, mActivity.findViewById(R.id.mainToolbarMessagesMore));
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
                startActivity(new Intent(mActivity, SeekActivity.class));
                return true;
        }
        return true;
    }

    private MainMessagesFragment getFragmentMessages(){
        if (fragmentMessages == null) fragmentMessages = MainMessagesFragment.newInstance();
        return fragmentMessages;
    }

    private MainContactsFragment getFragmentContacts(){
        if (fragmentContacts == null) fragmentContacts = MainContactsFragment.newInstance();
        return fragmentContacts;
    }

    private MainExploreFragment getFragmentExplore(){
        if (fragmentExplore == null) fragmentExplore = MainExploreFragment.newInstance();
        return fragmentExplore;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()){
            case R.id.mainBottomNavContacts:
                fragment = getFragmentContacts();
                break;
            case R.id.mainBottomNavExplore:
                fragment = getFragmentExplore();
                break;
            case R.id.mainBottomNavMessages: default:
                fragment = getFragmentMessages();
                break;
        }
        // 切换对应的页面
        getFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, fragment).commit();
        return true;
    }

}
