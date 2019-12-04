package com.aclass.android.qq.main;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aclass.android.qq.R;
import com.aclass.android.qq.common.ProfileUtil;
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
    private Fragment currentFragment;
    private MainFragmentViewModel mViewModel;

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

        mViewModel = ViewModelProviders.of(this).get(MainFragmentViewModel.class);

        if (Attribute.isAccountInitialized){
            bindData();
        } else {
            MainActivity.endowAccount(new Runnable() {
                @Override
                public void run() {
                    bindData();
                }
            });
        }

        // 点击头像切换至抽屉页面
        mViews.mainNavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.setPagerItem(0);
            }
        });
        mViews.mainBottomNav.setItemIconTintList(null);
        // 导航栏点击事件监听器，进行页面切换
        mViews.mainBottomNav.setOnNavigationItemSelectedListener(this);
        // 好友列表初始化在“联系人”里面
        if (mViewModel.itemId == 0) mViewModel.itemId = R.id.mainBottomNavContacts;
        mViews.mainBottomNav.setSelectedItemId(mViewModel.itemId);
    }

    private void bindData(){
        if (mViewModel.accountProfilePhoto != null) {
            mViews.mainNavIcon.setImageBitmap(mViewModel.accountProfilePhoto);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mViewModel.accountProfilePhoto = ProfileUtil.getRoundProfilePhoto(getContext(), null, null);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViews.mainNavIcon.setImageBitmap(mViewModel.accountProfilePhoto);
                        }
                    });
                }
            }).start();
        }
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

    private MainPage getNavPage(MenuItem item){
        Fragment fragment = getNavFragment(item);
        return (MainPage) fragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mViewModel.itemId = item.getItemId();
        Fragment fragment = getNavFragment(item);
        if (fragment == currentFragment) return true;

        // 以下：切换对应的页面
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        // 隐藏当前显示的页面
        if (currentFragment != null) transaction.hide(currentFragment);
        // 若已添加该 fragment 则将它显示出来；否则添加该 fragment
        if (fragment.isAdded()) transaction.show(fragment);
        else transaction.add(R.id.mainFragmentContainer, fragment);
        // 执行操作
        transaction.commit();
        // 当前显示的页面
        currentFragment = fragment;

        Menu menu = mViews.mainToolbar.getMenu();
        // 清除工具栏选项
        menu.clear();
        final MainPage page = (MainPage) fragment;
        page.onPageVisible(mViews.mainToolbar, mViews.mainToolbarTitle);

        // 以下：将工具栏选项图标颜色设置为白色
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
        MainPage page = getNavPage(item);
        page.onVisiblyClick();
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
         * 若当前页面已处于顶部，可以进行刷新（双击 tab 事件）
         */
        void onVisiblyClick();

//        /**
//         * 用于标识 fragment
//         * @return tag
//         */
//        String getManageableTag();
    }

}
