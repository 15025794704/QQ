package com.aclass.android.qq.main;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aclass.android.qq.R;
import com.aclass.android.qq.common.GraphicsUtil;
import com.aclass.android.qq.custom.GeneralFragment;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.FragmentMainBinding;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.main.contacts.MainContactsFragment;
import com.aclass.android.qq.main.explore.MainExploreFragment;
import com.aclass.android.qq.main.messages.MainMessagesFragment;

/**
 * 应用最主要的页面
 * 此 fragment 只有顶部工具栏和底部导航栏，导航栏对三个页面进行切换：
 * 1. {@link MainMessagesFragment “消息”页面}
 * 2. {@link MainContactsFragment “联系人”页面}
 * 3. {@link MainExploreFragment “动态”页面}
 */
public class MainFragment extends GeneralFragment implements BottomNavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener {
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Attribute.currentAccountProfilePhoto == null) return;
                Bitmap navIcon = GraphicsUtil.round(Attribute.currentAccountProfilePhoto);
                mViews.mainNavIcon.setImageBitmap(navIcon);
            }
        }, 600);
        mViews.mainBottomNav.setItemIconTintList(null);
        // 导航栏点击事件监听器，进行页面切换
        mViews.mainBottomNav.setOnNavigationItemSelectedListener(this);
        // 显示消息页面
        onNavigationItemSelected(mViews.mainBottomNav.getMenu().findItem(R.id.mainBottomNavMessages));
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar toolbar = mViews.mainToolbar;
        toolbar.setPadding(toolbar.getPaddingStart(), insets.top, toolbar.getPaddingEnd(), toolbar.getPaddingBottom());
        BottomNavigationView bottomNav= mViews.mainBottomNav;
        bottomNav.setPadding(bottomNav.getPaddingStart(), bottomNav.getPaddingTop(), bottomNav.getPaddingEnd(), insets.bottom);
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

    private Fragment getNavFragment(MenuItem item){
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
        return fragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = getNavFragment(item);
        // 切换对应的页面
        getFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, fragment).commit();
        final MainPage page = (MainPage) fragment;
        Menu menu = mViews.mainToolbar.getMenu();
        menu.clear();
        page.onPageVisible(mViews.mainToolbar, mViews.mainToolbarTitle);

        int mMenuIconTint = Color.WHITE;
        for (int i = 0; i < menu.size(); i++){
            MenuItem menuItem = menu.getItem(i);
            Drawable icon = menuItem.getIcon();
            if (icon == null) continue;
            icon.setTint(mMenuIconTint);
        }
        Drawable overflowIcon = mViews.mainToolbar.getOverflowIcon();
        if (overflowIcon != null) overflowIcon.setTint(mMenuIconTint);
        return true;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        Fragment fragment = getNavFragment(item);
        MainPage page = (MainPage) fragment;
        page.onVisiblyClick();
        // todo onVisiblyDoubleClick
    }

    public interface MainPage{

        /**
         * 切换至当前页面
         * 应当更新标题和工具栏选项
         */
        void onPageVisible(MyToolbar toolbar, TextView title);

        /**
         * 当前页面时，单击 tab
         * 应当回到顶部等
         */
        void onVisiblyClick();

        /**
         * 当前页面时，双击 tab
         * 应当刷新等
         * @return 是：已消化双击事件，否：调用 {@link #onVisiblyClick() onVisiblyClick}
         */
        boolean onVisiblyDoubleClick();
    }

}
