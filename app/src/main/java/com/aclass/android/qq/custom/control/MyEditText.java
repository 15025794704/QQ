package com.aclass.android.qq.custom.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;
/**
 * Created by Administrator on 2019/11/13.
 */

public class MyEditText extends EditText {
    public MyEditText(Context context) {
        super(context);
    }
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void insertDrawable(Bitmap bitmap,String spannableString) {
        SpannableString ss = new SpannableString(spannableString);
        //用这个drawable对象代替字符串easy
        ImageSpan span = new ImageSpan(bitmap);
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        append(ss);
    }
}
