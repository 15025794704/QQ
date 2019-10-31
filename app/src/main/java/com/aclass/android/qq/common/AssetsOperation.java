package com.aclass.android.qq.common;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import static javax.xml.transform.OutputKeys.ENCODING;

/**
 * Created by Administrator on 2019/10/31.
 */

//单例模式，获取assets文件夹的文件资源
public class AssetsOperation {
    private static AssetsOperation instance;
    private static AssetManager am;

    private AssetsOperation(){

    }

    /**
     * 获取AssetsOperation的实例
     * @param context
     * @return
     */
    public static AssetsOperation getInstance(Context context){
        if(instance==null)
            instance = new AssetsOperation();
        if(am==null)
            am=context.getAssets();
        return instance;
    }
    /**
     * 获取assets文件夹下面的文本资源
     * @param fileName
     * @return
     */
    public  String getText(String fileName){
        String result=null;
        try {
            InputStream in = am.open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[]  buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result =new String(buffer);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 获取assets文件夹下面的图片资源
     * @param fileName
     * @return
     */
    public  Bitmap getImage(String fileName){
        Bitmap result=null;
        try {
            InputStream in = am.open(fileName);
            result = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取assets文件夹下面的音频
     * @param fileName
     * @return
     */
    public AssetFileDescriptor getMusic(String fileName){
        AssetFileDescriptor result=null;
        try {
            result =am.openFd(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
