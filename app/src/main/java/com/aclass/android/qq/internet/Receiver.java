package com.aclass.android.qq.internet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.aclass.android.qq.MessageWindowActivity;
import com.aclass.android.qq.VideoWindowActivity;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.Message;
import com.aclass.android.qq.entity.MsgList;
import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.main.messages.MainMessagesFragment;
import com.aclass.android.qq.tools.MyDateBase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/11/6.
 */

/**
 * 由主进程调用
 * 消息接收器
 */
public class Receiver {

    public static void startReceiver(final Context context,final Activity activity){

        Attribute.msgArrayList=new ArrayList<>();

        Attribute.restartMessageReceive=new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                try {
                    MainMessagesFragment.readFile(activity);
                    boolean isWriteQun=false;
                   for(MsgList msg:Attribute.msgList){
                       if(msg.getQQFriend().length()==8) {
                           isWriteQun = true;
                           break;
                       }
                   }
                    if(!isWriteQun)
                        MainMessagesFragment.addWeQun(activity);
                }
                catch (Exception e){e.printStackTrace();}

                receive(context,activity);
                while(true) {
                    try {
                        getLastMsg(context);
                        SystemClock.sleep(2*60*1000);
//                        SystemClock.sleep(20*1000);
                        Attribute.mainMessageReceive.destroy();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        try {
                            receive(context,activity);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        Attribute.restartMessageReceive.start();

    }

    private static void receive(final Context context,final Activity activity){
        Attribute.mainMessageReceive=new Thread(new Runnable() {//发送服务器登录信息
            @Override
            public void run() {
                DatagramSocket receiveSocket;
                try {
                    receiveSocket = new DatagramSocket();
                    byte[] buf;
                    User user = new User();
                    if(Attribute.QQ==null || Attribute.QQ.equals(""))
                        SystemClock.sleep(2000);
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
                                    try {
                                        int port = Integer.parseInt(Attribute.friendMessageRequest.getSql());
                                        MyDateBase dateBase=new MyDateBase();
                                        dateBase.UDPsend(port,new byte[0]);
                                        dateBase.Destory();
                                    }
                                    catch (Exception e){e.printStackTrace();
                                    }finally {
                                        Message msg = (Message) Attribute.friendMessageRequest.getObj();
                                        if (Attribute.insertQQview != null && Attribute.insertQQview.equals(msg.getSendQQ())) {
                                            Attribute.msgArrayList.add(msg);
                                        }
                                        Receiver.writeMessageToFile(context, msg, msg.getSendQQ());
                                        setPoint(msg.getSendQQ(), true);
                                        Attribute.isReadMsgListFile=false;
                                        if (changeIndex(msg.getSendQQ())) {
                                            writeMsgListToFile(context);
                                        }
                                    }
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
        Attribute.mainMessageReceive.start();
    }

    //获取历史消息
    private static void getLastMsg(Context context){
        try {
            MyDateBase myDateBase = new MyDateBase();
            List<Message> msgList = myDateBase.getMessages(Attribute.QQ);
            for (Message msg : msgList) {
                writeMessageToFile(context, msg, msg.getSendQQ());
                changeIndex(msg.getSendQQ());
                setPoint(msg.getSendQQ(), true);
            }
            if(msgList!=null && msgList.size()!=0)
                myDateBase.updateMessage(Attribute.QQ);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            writeMsgListToFile(context);
        }
    }

    public  static void writeMessageToFile(Context context,Message msg,String QQFriend){
        try {
            String time="null";
            if(msg.getTime()!=null)
                time=msg.getTime().toLocaleString();
            FileOutputStream fos = context.openFileOutput(Attribute.QQ+QQFriend + ".json", Context.MODE_APPEND);
            String json="{\"sendQQ\":\""+msg.getSendQQ()+"\",\"receiveNum\":\""+msg.getReceiveNum()+
                    "\",\"context\":\""+msg.getContext()+"\",\"sendTime\":\""+time+"\"},";
            fos.write(json.getBytes());
            fos.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public  static void writeMsgListToFile(Context context){
        try {
            Attribute.isReadMsgListFile=true;
            List<MsgList> msgList=Attribute.msgList;
            if(msgList!=null) {
                FileOutputStream fos = context.openFileOutput(Attribute.QQ + "messageList.json", Context.MODE_PRIVATE);
                for (int i = 0; i < msgList.size(); i++) {
                    if (msgList.get(i).getIndex() != -1) {
                        String json = "{\"name\":\"" + msgList.get(i).getName() + "\",\"time\":\"" + msgList.get(i).getTime() +
                                "\",\"QQFriend\":\"" + msgList.get(i).getQQFriend() +
                                "\",\"point\":" + msgList.get(i).isPoint() +
                                ",\"top\":" + msgList.get(i).isTop()
                                + ",\"index\":" + i + "},";
                        fos.write(json.getBytes());
                        fos.flush();
                        Log.d("TAG", json);
                    }
                }
                fos.close();
            }
        }
        catch (IOException e){
            Log.e("TAG",e.toString());
            e.printStackTrace();
        }
    }

    public static boolean changeIndex(String QQFriend){
        int qqIndex= getIndexByQQ(QQFriend);
        int topMc=getMaxTopCount();
        Date d=new Date();
        if(topMc==qqIndex && qqIndex!=-1){
            return false;
        }
        if(qqIndex==-1){
            Friend f= Attribute.friendList.get(QQFriend);
            MsgList m= new MsgList(f.getBeiZhu(),d.getHours()+":"+d.getMinutes(),QQFriend,topMc,true);
            Attribute.msgList.add(topMc,m);
        }
        else{
            MsgList m= Attribute.msgList.get(qqIndex);
            m.setTime(d.getHours()+":"+d.getMinutes());
            Attribute.msgList.set(qqIndex,m);
            if(m.isTop())
                return false;
            m.setIndex(topMc);
            Attribute.msgList.remove(qqIndex);
            Attribute.msgList.add(topMc,m);
        }
        return true;
    }

    public static int getIndexByQQ(String QQFriend){
        for(int i=0;i<Attribute.msgList.size();i++){
            if(Attribute.msgList.get(i).getQQFriend().equals(QQFriend)){
                return i;
            }
        }
        return -1;
    }

    public static int getMaxTopCount(){
        int c=0;
        for(int i=0;i<Attribute.msgList.size();i++){
            if(Attribute.msgList.get(i).isTop()){
                c++;
            }
            else
                break;
        }
        return c;
    }

    public static void setPoint(String QQ,boolean point){
        int index= getIndexByQQ(QQ);
        if(index!=-1) {
            MsgList msg = Attribute.msgList.get(index);
            msg.setPoint(point);
            Attribute.msgList.set(index, msg);
        }
    }

    public static Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
