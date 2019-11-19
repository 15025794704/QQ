package com.aclass.android.qq.chat.contact;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

public class ContactChatSettingsViewModel extends ViewModel {
    MutableLiveData<ContactSettings> contactSettings = new MutableLiveData<>();
    MutableLiveData<Bitmap> friendProfilePhoto = new MutableLiveData<>();
}
