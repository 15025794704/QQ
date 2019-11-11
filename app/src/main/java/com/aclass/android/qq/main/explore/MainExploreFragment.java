package com.aclass.android.qq.main.explore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aclass.android.qq.R;

public class MainExploreFragment extends Fragment {

    public static MainExploreFragment newInstance(){
        return new MainExploreFragment();
    }

    public MainExploreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_explore, container, false);
    }

}
