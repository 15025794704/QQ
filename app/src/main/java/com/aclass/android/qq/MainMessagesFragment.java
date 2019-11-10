package com.aclass.android.qq;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainMessagesFragment extends Fragment {

    public static MainMessagesFragment newInstance(){
        return new MainMessagesFragment();
    }
    
    public MainMessagesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_messages, container, false);
    }

}
