package com.aclass.android.qq.main.explore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aclass.android.qq.R;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.main.MainFragment;

/**
 * 应用“动态”页面
 */
public class MainExploreFragment extends Fragment implements MainFragment.MainPage {

    public static MainExploreFragment newInstance(){
        return new MainExploreFragment();
    }

    public MainExploreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_explore, container, false);

    }

    @Override
    public void onPageVisible(MyToolbar toolbar, TextView title) {
        title.setText(R.string.mainBottomNavExplore);
    }

    @Override
    public void onVisiblyClick() {

    }

    @Override
    public void onVisiblyDoubleClick() {

    }
}
