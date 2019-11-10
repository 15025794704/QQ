package com.aclass.android.qq.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.aclass.android.qq.MainContactsFragment;
import com.aclass.android.qq.MainExploreFragment;
import com.aclass.android.qq.MainMessagesFragment;
import com.aclass.android.qq.R;
import com.aclass.android.qq.custom.GeneralFragment;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.FragmentMainBinding;
import com.aclass.android.qq.seek.SeekActivity;

import java.lang.reflect.InvocationTargetException;

public class MainFragment extends GeneralFragment implements Toolbar.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private FragmentMainBinding mViews;
    private MainActivity mActivity;
    private MainMessagesFragment fragmentMessages;
    private MainContactsFragment fragmentContacts;
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
        mViews.mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.setPagerItem(0);
            }
        });
        mViews.mainToolbar.setOnMenuItemClickListener(this);
        mViews.mainToolbar.setOverflowIcon(getContext().getDrawable(R.drawable.ic_add_24));
        mViews.mainBottomNav.setOnNavigationItemSelectedListener(this);
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
        getFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, fragment).commit();
        return true;
    }

}
