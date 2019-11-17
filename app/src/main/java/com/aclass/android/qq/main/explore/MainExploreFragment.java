package com.aclass.android.qq.main.explore;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aclass.android.qq.R;

/**
 * 应用“动态”页面
 */
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
