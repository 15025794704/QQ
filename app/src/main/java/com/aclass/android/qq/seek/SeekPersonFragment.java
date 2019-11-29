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
import android.widget.Toast;

import com.aclass.android.qq.databinding.FragmentSeekPersonBinding;
import com.aclass.android.qq.entity.Qun;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
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
                    boolean isSelf = input.equals(Attribute.currentAccount.getQQNum());
                    MyDateBase database = isSelf ? null : new MyDateBase();
                    final User user = isSelf ? null : database.getUser(input);
                    Qun group = isSelf ? null : database.getQun(input);
                    if (!isSelf) database.Destory();
                    boolean isContactExist = isSelf || user != null;
                    boolean isContactAdded = !isSelf && isContactExist && Attribute.friendList.containsKey(user.getQQNum());
                    String errorMessage = null;
                    if (isSelf) {
                        errorMessage = "不能添加自己哦";
                    } else if (!isContactExist) {
                        errorMessage = "账号不存在";
                    } else if (isContactAdded) {
                        errorMessage = "你与该账号已经是好友啦";
                    }
                    if (errorMessage != null) {
                        final String message = errorMessage;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    final Intent intent = new Intent(context, NewContactActivity.class);
                    intent.putExtra(NewContactActivity.ARG_CONTACT, (Parcelable) user);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                        }
                    });
                }
            }).start();
        }
    }
}
