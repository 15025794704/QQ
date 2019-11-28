package com.aclass.android.qq;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.common.AssetsOperation;
import com.aclass.android.qq.common.MyBitMapOperation;
import com.aclass.android.qq.common.MyButtonOperation;
import com.aclass.android.qq.common.ProfileUtil;
import com.aclass.android.qq.common.Screen;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.RoundImageView;
import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class VideoWindowActivity extends GeneralActivity implements TextureView.SurfaceTextureListener{

    AssetsOperation assetsOperation;
    Screen screen;
    ImageButton btn_refuse;
    ImageButton btn_refuse_come;
    ImageButton btn_accept_come;
    ImageButton btn_mic;
    ImageButton btn_loudspeaker;
    ImageButton btn_mini;

    RoundImageView headImg;
    private LinearLayout info;

    private Camera mCamera;
    private TextureView textureView;
    private ImageView videoView;

    private Thread  videoThread;
    private Thread  sendVideoThread;
    private WifiManager.MulticastLock lock=null;
    private MyDateBase sendVideoDataBase;
    private MyDateBase receiveVideoDataBase;
    private int ServerPort;
    private String QQfriend;
    private String videoType;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (msg.what == 0x11) {
                Bitmap bmp=byte2Bitmap(Attribute.video_bitmap);
                bmp= MyBitMapOperation.rotateBitmap(bmp,270);
                bmp=MyBitMapOperation.flipBitmap(bmp);
                videoView.setImageBitmap(bmp);
            }
            else if(msg.what==0x12){
                Toast.makeText(VideoWindowActivity.this,bundle.getString("msg"),Toast.LENGTH_LONG).show();
            }
            else if(msg.what == 0x13){
                finish();
            }
            else if(msg.what == 0x14){
                info.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_video);

        WifiManager manager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        lock= manager.createMulticastLock("test wifi");

        String[] args=getIntent().getStringArrayExtra("args");
        QQfriend=args[1];
        videoType=args[0];

        Attribute.video_bitmap_send=bitmap2Bytes(ProfileUtil.getDefaultProfilePhoto(this));
        init();
        initData();
        set_btn_click();
        initVideoThread();
    }

    @Override
    protected void consumeInsets(Rect insets) {
        LinearLayout l=(LinearLayout) findViewById(R.id.LinearLayout_video_window);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,insets.top,0,0);
        l.setLayoutParams(layoutParams);
    }

    private void initVideoThread(){

        videoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Attribute.isInVideo=true;
                    Request request= Attribute.friendVideoRequest;
                    com.aclass.android.qq.entity.Message msg=(com.aclass.android.qq.entity.Message)request.getObj();

                    receiveVideoDataBase.setTimeout(5000);
                    msg.setContext("2>1");//接受
                    request.setObj(msg);
                    receiveVideoDataBase.UDPsend(request);    //发送拒绝或接受

                        byte[] b= receiveVideoDataBase.receiveData();
                        final int port=Integer.parseInt(new String(b));//服务器视频端口
                    ServerPort=port;
                        ActivityOpreation.updateUI(handler, 0x12, "连接成功");
                         ActivityOpreation.updateUI(handler, 0x14, "");//隐藏信息

                        SystemClock.sleep(1000);
                    receiveVideoDataBase.UDPsend(port,"");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                addCallBack();
                                SystemClock.sleep(300);
                                try {
                                    for (; ; ) {
                                        if (!Attribute.isInVideo) {
                                            receiveVideoDataBase.Destory();
                                            return;
                                        }
                                        receiveVideoDataBase.UDPsend(port, Attribute.video_bitmap_send);
                                        SystemClock.sleep(125);
                                    }
                                }
                                catch (Exception e1){return;}
                            }
                        }).start();

                        receiveVideoDataBase.setTimeout(5000);
                        for (; ; ) {
                            if(!Attribute.isInVideo) {
                                return;
                            }
                            byte[] bt= receiveVideoDataBase.receiveData();
                            if( bt.length==0){
                                receiveVideoDataBase.Destory();
                                Attribute.isInVideo=false;
                                ActivityOpreation.updateUI(handler, 0x12,"已挂断");
                                ActivityOpreation.updateUI(handler, 0x13,"");
                                return;
                            }
                            Attribute.video_bitmap=bt;
                            ActivityOpreation.updateUI(handler, 0x11, "");
                        }
                }
                catch (Exception e){
                    Attribute.isInVideo=false;
                    finish();
                    return;
                }

            }
        });

    }

    private void sendVideoRequest(String QQFriend){
        try {
            SystemClock.sleep(1000);
            com.aclass.android.qq.entity.Message msg = new com.aclass.android.qq.entity.Message();
            msg.setSendQQ(Attribute.QQ);
            msg.setReceiveNum(QQFriend);
            msg.setContext("request");
            Request request = new Request(8, "", msg);

            sendVideoDataBase = new MyDateBase();
            sendVideoDataBase.setTimeout(15000);
            sendVideoDataBase.UDPsend(request);

            request=(Request) sendVideoDataBase.receiveObject();
            if(request.getRequestType()==9){
                ActivityOpreation.updateUI(handler, 0x12, "好友已拒绝");
                sendVideoDataBase.Destory();
                return;
            }
            else if(request.getRequestType()==10){
                ActivityOpreation.updateUI(handler, 0x12, "好友不在线");
                sendVideoDataBase.Destory();
                return;
            }

            sendVideoDataBase.setTimeout(5000);
            Attribute.isInVideo=true;
            byte[] b= sendVideoDataBase.receiveData();
            final  int port=Integer.parseInt(new String(b));//服务器视频端口
            ServerPort=port;
            ActivityOpreation.updateUI(handler, 0x12, "搭建连接成功"+port);
            ActivityOpreation.updateUI(handler, 0x14, "");//隐藏信息

            sendVideoDataBase.UDPsend(port,"");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    addCallBack();
                    SystemClock.sleep(1375);
                    try {
                        for(;;) {
                            if(!Attribute.isInVideo) {
                                sendVideoDataBase.Destory();
                                return;
                            }
                            if(Attribute.video_bitmap_send!=null)
                                sendVideoDataBase.UDPsend(port, Attribute.video_bitmap_send);
                            SystemClock.sleep(125);
                        }
                    }catch (Exception e1){return;}


                }
            }).start();

            sendVideoDataBase.setTimeout(5000);
            for (; ; ) {
                if(!Attribute.isInVideo ) {
                    sendVideoDataBase.Destory();
                    return;
                }
                byte[] bt= sendVideoDataBase.receiveData();
                if( bt.length==0){
                    sendVideoDataBase.Destory();
                    Attribute.isInVideo=false;
                    ActivityOpreation.updateUI(handler, 0x12,"已挂断");
                    ActivityOpreation.updateUI(handler, 0x13,"");
                    return;
                }
                Attribute.video_bitmap=bt;
                ActivityOpreation.updateUI(handler, 0x11,"");
            }
        }
        catch (Exception e){
            Attribute.isInVideo=false;
            finish();
            return;
        }
    }

    protected void set_btn_click(){
        btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送断开请求
                close();
            }
        });
        btn_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_accept_come.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.video_show_send)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.video_show_come)).setVisibility(View.INVISIBLE);
                videoThread.start();//接受
            }
        });
        btn_refuse_come.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Request request= Attribute.friendVideoRequest;
                            com.aclass.android.qq.entity.Message msg=(com.aclass.android.qq.entity.Message)request.getObj();

                            receiveVideoDataBase.setTimeout(10000);
                            msg.setContext("refuse");//拒绝
                            request.setObj(msg);
                            receiveVideoDataBase.UDPsend(request);    //发送拒绝或接受
                            receiveVideoDataBase.Destory();
                            ActivityOpreation.updateUI(handler,0x13,"");//finish
                        }
                    }).start();
                }
             catch (Exception e){}
            }
        });
    }

    private void close(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (sendVideoDataBase != null) {
                            sendVideoDataBase.UDPsend(ServerPort, new byte[0]);
                            SystemClock.sleep(30);
                            sendVideoDataBase.UDPsend(ServerPort, new byte[0]);
                        }
                        if (receiveVideoDataBase != null) {
                            receiveVideoDataBase.UDPsend(ServerPort, new byte[0]);
                            SystemClock.sleep(30);
                            receiveVideoDataBase.UDPsend(ServerPort, new byte[0]);
                        }
                        Attribute.isInVideo = false;
                        if (videoThread != null)
                            videoThread.stop();
                        if (sendVideoThread != null)
                            sendVideoThread.stop();
                    } catch (Exception e) {
                    } finally {
                        finish();
                    }
                }
            }).start();
        }
        catch (Exception e){}
        finally {
            finish();
        }
    }

    protected void init(){

        if(videoType.equals("send")){
            ((LinearLayout) findViewById(R.id.video_show_send)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.video_show_come)).setVisibility(View.INVISIBLE);
            sendVideoThread= new Thread(new Runnable() {
                @Override
                public void run() {
                    sendVideoRequest(QQfriend);
                }
            });
            sendVideoThread .start();
        }
        else {
            ((LinearLayout) findViewById(R.id.video_show_send)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.video_show_come)).setVisibility(View.VISIBLE);
            receiveVideoDataBase=new MyDateBase();
        }
            //设置状态栏背景
      //  ActivityOpreation.setStatusBar(this,R.color.colorVideoViewBG,View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //初始化对象
        screen=new Screen(this);
        assetsOperation=AssetsOperation.getInstance(this);
        btn_refuse=(ImageButton)findViewById(R.id.image_btn_refuse);
        btn_mic=(ImageButton)findViewById(R.id.image_btn_mic);
        btn_loudspeaker=(ImageButton)findViewById(R.id.image_btn_loudspeaker);
        btn_mini=(ImageButton)findViewById(R.id.image_btn_mini);
        btn_refuse_come=(ImageButton)findViewById(R.id.image_btn_come_refuse);
        btn_accept_come=(ImageButton)findViewById(R.id.image_btn_come_accept);
        headImg=(RoundImageView)findViewById(R.id.RoundImageView_video_head);
        textureView=(TextureView) findViewById(R.id.texture_video_ImageView);
        TextView name=(TextView) findViewById(R.id.textView);
        videoView=(ImageView) findViewById(R.id.video_ImageView);
        info=(LinearLayout)findViewById(R.id.LinearLayout_video_window_info);
        //videoView.setRotation(270);


        //头像框设置大小,图片资源
        int width=(int)(screen.getposWidth()*0.38);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
        headImg.setLayoutParams(params);
        if(Attribute.userHeadList.get(QQfriend)!=null)
            headImg.setImageBitmap(Attribute.userHeadList.get(QQfriend));
        if(Attribute.friendList.get(QQfriend)!=null)
            name.setText(Attribute.friendList.get(QQfriend).getBeiZhu());
        //设置拒绝按钮按下和弹起效果
        MyButtonOperation.changeImageButton(this,btn_refuse,R.drawable.btn_refuse_2,R.drawable.btn_refuse_1);
        //设置小化按钮按下和弹起效果
        MyButtonOperation.changeImageButton(this,btn_mini,R.drawable.videoview_mini_btn2,R.drawable.videoview_mini_btn);
        MyButtonOperation.changeImageButton(this,btn_accept_come,R.drawable.video_accept2,R.drawable.video_accept2);
        MyButtonOperation.changeImageButton(this,btn_refuse_come,R.drawable.btn_refuse_2,R.drawable.btn_refuse_1);

    }

    private void initData() {
        int numberOfCameras = Camera.getNumberOfCameras();// 获取摄像头个数
        if(numberOfCameras<1){
            Toast.makeText(this, "没有相机", Toast.LENGTH_SHORT).show();
//            finish();
            return;
        }
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        // 打开相机 0后置 1前置
        mCamera = Camera.open(1);
        if (mCamera != null) {
            // 设置相机预览宽高，此处设置为TextureView宽高
            Camera.Parameters params = mCamera.getParameters();
            params.setPreviewSize(width, height);
            chooseFixedPreviewFps(params,20);
            // 设置自动对焦模式
            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                mCamera.setParameters(params);
            }
            try {
                mCamera.setDisplayOrientation(90);// 设置预览角度，并不改变获取到的原始数据方向
                // 绑定相机和预览的View
                mCamera.setPreviewTexture(surface);
                // 开始预览
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addCallBack() {
        if(mCamera!=null){
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    try{
                        Camera.Size size = camera.getParameters().getPreviewSize();
                        YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                        if(image!=null){
                            if(!Attribute.isInVideo) {
                                sendVideoDataBase.Destory();
                                return;
                            }
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            image.compressToJpeg(new Rect(0, 0, size.width,size.height), 16, stream);
                            byte[] b=stream.toByteArray();
                            Attribute.video_bitmap_send=b;
                            stream.close();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        return;
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy(){
        try {
            Attribute.isInVideo=false;
            if (videoThread != null)
                videoThread.stop();
            if(sendVideoThread!=null)
                sendVideoThread.stop();
        }
        catch (Exception e){}
        super.onDestroy();
    }

    /**
     * 选择合适的FPS
     * @param parameters
     * @param expectedThoudandFps 期望的FPS
     * @return
     */
    public static int chooseFixedPreviewFps(Camera.Parameters parameters, int expectedThoudandFps) {
        List<int[]> supportedFps = parameters.getSupportedPreviewFpsRange();
        for (int[] entry : supportedFps) {
            if (entry[0] == entry[1] && entry[0] == expectedThoudandFps) {
                parameters.setPreviewFpsRange(entry[0], entry[1]);
                return entry[0];
            }
        }
        int[] temp = new int[2];
        int guess;
        parameters.getPreviewFpsRange(temp);
        if (temp[0] == temp[1]) {
            guess = temp[0];
        } else {
            guess = temp[1] / 2;
        }
        return guess;
    }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return false;
    }
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {}

    private byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private Bitmap byte2Bitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
