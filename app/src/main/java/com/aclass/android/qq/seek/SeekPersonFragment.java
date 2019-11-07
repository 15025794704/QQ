package com.aclass.android.qq.seek;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.aclass.android.qq.databinding.FragmentSeekPersonBinding;
import com.aclass.android.qq.entity.Qun;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.tools.MyDateBase;

public class SeekPersonFragment extends Fragment {
    private FragmentSeekPersonBinding mViews;

    public SeekPersonFragment() {
    }

    public static SeekPersonFragment newInstance() {
        return new SeekPersonFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViews = FragmentSeekPersonBinding.inflate(inflater, container, false);
        return mViews.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Context context = getContext();
        mViews.seekPersonNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE){
                    seek(context);
                    return true;
                }
                return false;
            }
        });
    }

    private void seek(final Context context){
        final String input = mViews.seekPersonNum.getText().toString();
        if (input.isEmpty()) return;
        if (input.matches("\\d+")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyDateBase dateBase = new MyDateBase();
                    final User user = dateBase.getUser(input);
                    Qun group = dateBase.getQun(input);
                    dateBase.Destory();
                    if (user != null){
                        final Intent intent = new Intent(context, NewFriendActivity.class);
                        intent.putExtra(NewFriendActivity.ARG_CONTACT, (Parcelable) user);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                            }
                        });
                    }
                }
            }).start();
        }
    }
}
