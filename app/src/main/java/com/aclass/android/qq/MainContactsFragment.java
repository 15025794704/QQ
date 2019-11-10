package com.aclass.android.qq;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainContactsFragment extends Fragment {

    public static MainContactsFragment newInstance(){
        return new MainContactsFragment();
    }

    public MainContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_contacts, container, false);
    }

}
