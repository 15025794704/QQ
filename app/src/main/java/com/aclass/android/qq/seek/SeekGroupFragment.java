package com.aclass.android.qq.seek;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aclass.android.qq.databinding.FragmentSeekGroupBinding;

public class SeekGroupFragment extends Fragment {
    private FragmentSeekGroupBinding mViews;

    public SeekGroupFragment() {
    }

    public static SeekGroupFragment newInstance(){
        return new SeekGroupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViews = FragmentSeekGroupBinding.inflate(inflater, container, false);
        return mViews.getRoot();
    }

}
