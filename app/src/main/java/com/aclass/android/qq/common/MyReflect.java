package com.aclass.android.qq.common;

import android.os.SystemClock;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2019/10/24.
 */

public class MyReflect {
    /**
     * 开启子线程执行方法
     * @param owner  对象
     * @param methodName  方法
     * @param args  参数数组
     */
    private static Object returnObj=null;

    public static Object invokeFromThread(final Object owner, final String methodName, final Object[] args) {
        returnObj=null;
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    returnObj= invoke(owner, methodName, args);
                }
            }).start();
        } catch (Exception e) {
            Log.e("invokeFromThread错误","invokeFromThread错误");
            returnObj=null;
        }
        return returnObj;
    }

    /**
     * 用当前线程执行方法
     * @param owner  对象
     * @param methodName  方法
     * @param args  参数数组
     */
    public static Object  invoke(final Object owner,final String methodName,final Object[] args){
                    Method method;
                    try {
                        Class ownerClass = owner.getClass();

                        if (args == null) {
                            method = ownerClass.getMethod(methodName);
                            return method.invoke(owner);
                        } else {
                            Class[] argsClass = new Class[args.length];
                            for (int i = 0; i < args.length; i++) {
                                if(args[i]!=null) {
                                    argsClass[i] = args[i].getClass();
                                }
                            }
                            method = ownerClass.getMethod(methodName, argsClass);
                            return method.invoke(owner, args);
                        }
                    } catch (Exception e) {
                        Log.e("invoke错误","invoke错误");
                    }
        return null;
    }

    /**
     * 延时执行对象方法
     * @param owner  对象
     * @param methodName   方法
     * @param args  参数数组
     * @param time  延时时间
     */
    public static void  delayMethod(final Object owner,final String methodName,final Object[] args,final int time){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(time);
                    invoke(owner,methodName,args);
                }
            }).start();
        }
        catch (Exception e){
            Log.e("delayMethod错误","delayMethod错误");
        }
    }
}
