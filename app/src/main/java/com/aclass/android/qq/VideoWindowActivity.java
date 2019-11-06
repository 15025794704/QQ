package com.aclass.android.qq;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
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
import android.widget.Toast;

import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.common.AssetsOperation;
import com.aclass.android.qq.common.MyBitMapOperation;
import com.aclass.android.qq.common.MyButtonOperation;
import com.aclass.android.qq.common.Screen;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.RoundImageView;
import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.entity.*;
import com.aclass.android.qq.tools.MyDateBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.List;

public class VideoWindowActivity extends GeneralActivity implements TextureView.SurfaceTextureListener{

    AssetsOperation assetsOperation;
    Screen screen;
    ImageButton btn_refuse;
    ImageButton btn_mic;
    ImageButton btn_loudspeaker;
    ImageButton btn_mini;

    RoundImageView headImg;
//
//    int count=0;
//    long time=0;
    private Camera mCamera;
    private TextureView textureView;
    private ImageView videoView;

    private MyDateBase myDateBase;
    private Thread threadStartVideo;
    private DatagramSocket socket;
    private SocketAddress friendAddress;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (msg.what == 0x11) {
//                if(count==0){
//                    time=System.currentTimeMillis();
//                }
//                if(count==100){
//                    time=System.currentTimeMillis()-time;
//                    Toast.makeText(VideoWindowActivity.this,time+"",Toast.LENGTH_LONG).show();
//                }
//                count++;
                byte[] b= bundle.getByteArray("msg");
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_video);
        init();
        initData();
       set_btn_mini_click();
       set_btn_refuse_click();
        startThreadStartVideo();
    }

    @Override
    protected void consumeInsets(Rect insets) {
      LinearLayout l=(LinearLayout) findViewById(R.id.LinearLayout_video_window);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,insets.top,0,0);
        l.setLayoutParams(layoutParams);
    }

    private void startThreadStartVideo(){
        threadStartVideo=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SystemClock.sleep(800);
                    socket = new DatagramSocket();
                    User user = new User();
                    user.setQQNum("1505249457");
                    myDateBase.UDPsend(new Request(0, "", user));
                    SystemClock.sleep(500);
                    com.aclass.android.qq.entity.Message message = new com.aclass.android.qq.entity.Message();
                    message.setSendQQ("");
                    message.setReceiveNum("");
                    message.setContext("request");
                    myDateBase.UDPsend(new Request(8, "", message));
                    friendAddress = (SocketAddress) myDateBase.receiveObject();
                    addCallBack();
                }
                catch (Exception e){
                }
            }
        });
        threadStartVideo.start();
    }


    //最小化按钮执行方法
    protected void set_btn_mini_click(){
        btn_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               close();
            }
        });
    }

    //d挂断按钮执行方法
    protected void set_btn_refuse_click(){
        btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送断开请求
                close();
            }
        });
    }

    private void close(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    myDateBase.UDPsend(socket,friendAddress,MyDateBase.toByteArray(new Request(9,"close",null)));
                    threadStartVideo.stop();
                }
                catch (Exception e){
                }
                finally {
                    ActivityOpreation.updateUI(handler,0x13,"finish");
                }
            }
        }).start();
    }

    protected void init(){
        //设置状态栏背景
      //  ActivityOpreation.setStatusBar(this,R.color.colorVideoViewBG,View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //初始化对象
        screen=new Screen(this);
        assetsOperation=AssetsOperation.getInstance(this);
        btn_refuse=(ImageButton)findViewById(R.id.image_btn_refuse);
        btn_mic=(ImageButton)findViewById(R.id.image_btn_mic);
        btn_loudspeaker=(ImageButton)findViewById(R.id.image_btn_loudspeaker);
        btn_mini=(ImageButton)findViewById(R.id.image_btn_mini);
        headImg=(RoundImageView)findViewById(R.id.RoundImageView_video_head);
        textureView=(TextureView) findViewById(R.id.texture_video_ImageView);
        videoView=(ImageView) findViewById(R.id.video_ImageView);
        myDateBase=new MyDateBase();
        //videoView.setRotation(270);


        //头像框设置大小,图片资源
        int width=(int)(screen.getposWidth()*0.38);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
        headImg.setLayoutParams(params);
        headImg.setImageBitmap(assetsOperation.getImage("image/qq.png"));
        //设置拒绝按钮按下和弹起效果
        MyButtonOperation.changeImageButton(this,btn_refuse,R.drawable.btn_refuse_2,R.drawable.btn_refuse_1);
        //设置小化按钮按下和弹起效果
        MyButtonOperation.changeImageButton(this,btn_mini,R.drawable.videoview_mini_btn2,R.drawable.videoview_mini_btn);
    }

    private void initData() {
        int numberOfCameras = Camera.getNumberOfCameras();// 获取摄像头个数
        if(numberOfCameras<1){
            Toast.makeText(this, "没有相机", Toast.LENGTH_SHORT).show();
            finish();
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
            chooseFixedPreviewFps(params,30);
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
                    Camera.Size size = camera.getParameters().getPreviewSize();
                    try{
                        YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                        if(image!=null){
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            image.compressToJpeg(new Rect(0, 0, size.width,size.height), 80, stream);
                            byte[] b=stream.toByteArray();
                            ActivityOpreation.updateUI(handler,0x11,b);
                            stream.close();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
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

}
