package com.aclass.android.qq.chat.group;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

public class GroupChatSettingsViewModel extends ViewModel {
    MutableLiveData<GroupSettings> groupSettings = new MutableLiveData<>();
    MutableLiveData<Bitmap> groupProfilePhoto = new MutableLiveData<>();
}
