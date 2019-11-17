package com.aclass.android.qq.common;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.NonNull;

public class GraphicsUtil {

    public static Bitmap mutable(@NonNull Bitmap src){
        boolean isImmutable = !src.isMutable() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && src.getConfig() == Bitmap.Config.HARDWARE);
        return isImmutable ? src.copy(Bitmap.Config.ARGB_8888, true) : src;
    }

    public static Bitmap round(@NonNull Bitmap src){
        int width = src.getWidth();
        int height = src.getHeight();
        float radius = Math.min(width, height) * 0.5f;
        Paint paint = new  Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap bitmap = mutable(src);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        Bitmap circular = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new  Canvas(circular);
        canvas.drawCircle(width * 0.5f, height * 0.5f, radius, paint);
        return circular;
    }
}
