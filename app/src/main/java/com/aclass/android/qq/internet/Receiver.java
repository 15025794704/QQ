package com.aclass.android.qq.internet;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ScrollView;

import com.aclass.android.qq.MessageWindowActivity;
import com.aclass.android.qq.VideoWindowActivity;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.entity.Message;
import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.tools.MyDateBase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by Administrator on 2019/11/6.
 */

/**
 * 由主进程调用
 * 消息接收器
 */
public class Receiver {

    public static void startReceiver(final Context context,final Activity activity){
        if(Attribute.restartMessageReceive!=null)
            return;
        Attribute.restartMessageReceive=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        SystemClock.sleep(2*60*1000);
                        Attribute.mainMessageReceive.stop();
                    }
                    catch (Exception e){}
                    finally {
                        try {
                            Attribute.mainMessageReceive.start();
                        }
                        catch (Exception e){
                        }
                    }
                }
            }
        });

        Attribute.msgArrayList=new ArrayList<>();

        Attribute.mainMessageReceive=new Thread(new Runnable() {//发送服务器登录信息
            @Override
            public void run() {
                DatagramSocket receiveSocket;
                try {
                    receiveSocket = new DatagramSocket();
                    byte[] buf;
                    User user = new User();
                    user.setQQNum(Attribute.QQ);
                    Request request = new Request(0,"",user);
                    buf=MyDateBase.toByteArray(request);
                    receiveSocket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName("47.107.138.4"),890));
                    DatagramPacket dpReceive;

                    while (true) {
                        buf = new byte[1024*60];
                        dpReceive = new DatagramPacket(buf, buf.length);
//                        lock.acquire();
                        receiveSocket.receive(dpReceive);
                        request = (Request) MyDateBase.toObject(buf, dpReceive.getLength());
                        if (request.getRequestType() == 8) {
                            if(!Attribute.isInVideo) {
                                String send = ((Message) request.getObj()).getSendQQ();
                                Attribute.friendVideoRequest = request;
                                ActivityOpreation.jumpActivity(activity, VideoWindowActivity.class, new String[]{"receive", send});
                            }
                        }
                        else if(request.getRequestType()==5){
                            Attribute.friendMessageRequest=request;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Message msg=(Message) Attribute.friendMessageRequest.getObj();
                                    if(Attribute.insertQQview!=null && Attribute.insertQQview.equals(msg.getSendQQ())){
                                        Attribute.msgArrayList.add(msg);
                                    }
                                    Receiver.writeMessageToFile(context,msg,msg.getSendQQ());
                                }
                            }).start();
                        }
//                        lock.release();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Attribute.restartMessageReceive.start();
        Attribute.mainMessageReceive.start();
    }




    public  static void writeMessageToFile(Context context,Message msg,String QQFriend){
        try {
            FileOutputStream fos = context.openFileOutput(QQFriend + ".json", Context.MODE_APPEND);
            String json="{\"sendQQ\":\""+msg.getSendQQ()+"\",\"receiveNum\":\""+msg.getReceiveNum()+
                    "\",\"context\":\""+msg.getContext()+"\",\"sendTime\":\""+msg.getTime().toLocaleString()+"\"},";
            fos.write(json.getBytes());
            fos.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
