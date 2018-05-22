package com.example.zz_xa.firecontrol.VideoLive;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zz_xa.firecontrol.R;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * Created by Adminstrator of wxb on 2018/5/9.
 * Fix by:
 */

public class VideoLiveActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private static final String LOG_TAG = VideoLiveActivity.class.getSimpleName();
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;

    private RtcEngine mRtcEngine;

    private MediaRecorder mRecorder;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private String path;
    private SurfaceView mSurfaceview2;
    private Button mBtnStartStop;
    private boolean mStartedFlg = false;//是否正在录像

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onUserOffline(int uid, int reason) {

            //super.onUserOffline(uid, reason);
        }

        @Override
        public void onUserMuteVideo(int uid, boolean muted) {
           // super.onUserMuteVideo(uid, muted);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.video_live_activity);

        mBtnStartStop = (Button) findViewById(R.id.btnStartStop_activity);
        mSurfaceview2 = (SurfaceView) findViewById(R.id.surfaceview2);

        if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO,PERMISSION_REQ_ID_RECORD_AUDIO) && checkSelfPermission(android.Manifest.permission.CAMERA,PERMISSION_REQ_ID_CAMERA)) {
  //      if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO,PERMISSION_REQ_ID_RECORD_AUDIO) ) {
/*
            if(camera == null) {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
*/
            init();
         //   initShot();
        }

        mBtnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mStartedFlg){
                    initShot();
                } else {
                    if(mStartedFlg){
                        try {

                            mRecorder.stop();
                            mRecorder.reset();
                            mRecorder.release();
                            mRecorder = null;
                            mBtnStartStop.setText("Start");
                            if (mCamera != null) {
                                mCamera.release();
                                mCamera = null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mStartedFlg = false;
                }
            }
        });

       // SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        SurfaceHolder holder = mSurfaceview2.getHolder();

     //   SurfaceHolder holder = surfaceView.getHolder();

        holder.addCallback(this);
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }
     private void initShot(){
         if (mRecorder == null) {
             mRecorder = new MediaRecorder();
         }
         try {
             mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
         //    initCamera();
             if(mCamera != null){
                 mCamera.setDisplayOrientation(90); //开始录制前，预览角度
              //   mCamera.setPreviewDisplay(mSurfaceHolder);
             //    mCamera.startPreview();
                 mCamera.unlock();
                 mRecorder.setCamera(mCamera);
             }
             // 这两项需要放在setOutputFormat之前
             mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);//音频源
             mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//视频源

             // Set output file format
             mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//视频输出格式

             // 这两项需要放在setOutputFormat之后
             mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//音频格式
             mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);//视频录制格式

             //   mRecorder.setVideoSize(640, 480);  // 设置视频分辨率
             mRecorder.setVideoSize(640, 480);  // 设置视频分辨率
             //     mRecorder.setVideoFrameRate(30);//帧率，1秒30帧
             mRecorder.setVideoFrameRate(30);//帧率，1秒30帧
             //   mRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);//码流
             mRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);//码流
             mRecorder.setOrientationHint(90); //输出旋转90度，保持竖屏录制
             //设置记录会话的最大持续时间（毫秒）
             mRecorder.setMaxDuration(30 * 1000);
             mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

             path = getSDPath();
             if (path != null) {
                 File dir = new File(path + "/11/video");
                 if (!dir.exists()) {
                     dir.mkdir();
                 }
                 path = dir + "/" + getDate() + ".mp4";
                 mRecorder.setOutputFile(path);
                 mRecorder.prepare();
                 mRecorder.start();
                 mStartedFlg = true;
                 mBtnStartStop.setText("Stop");
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
      //   mSurfaceHolder = holder;
     }

     private void initCamera() throws IOException {
         if(mCamera != null) {
             freeCameraResource();
         }

         try {
             //mCamera = Camera.open();
             mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
         } catch (Exception e){
             e.printStackTrace();
             freeCameraResource();
         }

         if(mCamera == null){
             return;
         }

         mCamera.setDisplayOrientation(90);
         mCamera.setPreviewDisplay(mSurfaceHolder);
         mCamera.startPreview();
      //   mCamera.unlock();

         initShot();
     }

     private void freeCameraResource() {
         if(mCamera != null){
             mCamera.setPreviewCallback(null);
             mCamera.stopPreview();
             mCamera.lock();
             mCamera.release();
             mCamera = null;
         }
     }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
         mSurfaceview2 = null;
         mSurfaceHolder = null;

        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            Log.d(LOG_TAG, "surfaceDestroyed release mRecorder");
        }
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 获取系统时间
     *
     * @return
     */
    public static String getDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // 获取年份
        int month = ca.get(Calendar.MONTH);         // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE);       // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);       // 秒

        String date = "" + year + (month + 1) + day + hour + minute + second;
        Log.d(LOG_TAG, "date:" + date);

        return date;
    }
    /**
     * 获取SD path
     *
     * @return
     */
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取根目录
            return sdDir.toString();
        }

        return null;
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        if(ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{permission},requestCode);
            return false;
        }
        return true;
    }
    private void init() {
        String appId = "2aa669b898904ae78f5297598c14945a";

        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), appId, mRtcEventHandler);
        }catch (Exception e){
            Log.e(LOG_TAG, Log.getStackTraceString(e));
            throw new RuntimeException("need to check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }

        //打开视频模式
        mRtcEngine.enableVideo();
        //设置视频分辨率
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);

        FrameLayout container = (FrameLayout) findViewById(R.id.local_video_view_container);

        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());


    //    mSurfaceview2 = surfaceView;
   /*     SurfaceHolder holder = mSurfaceview2.getHolder();

        holder.addCallback(VideoLiveActivity.this);
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

*/
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);


     //   mSurfaceHolder = surfaceView.getHolder();
     //   mSurfaceHolder.addCallback(new CustomCallBack());
     //   mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));

        mRtcEngine.joinChannel(null,"zz", null,0);

        mRtcEngine.muteAllRemoteAudioStreams(true);


     //   initShot();
    }



    public void live_finish(View view) {
        try {
       //     handler.removeCallbacks(runnable);
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            mBtnStartStop.setText("Start");

            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
           freeCameraResource();
        } catch (Exception e) {
            e.printStackTrace();
        }

        finish();
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
    }
    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    public void onLocalAudioMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
            iv.clearColorFilter();
        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.blue_light), PorterDuff.Mode.MULTIPLY);
        }

        mRtcEngine.muteLocalAudioStream(iv.isSelected());
    }


    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();

        // initShot();
    }
    private class CustomCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                initCamera();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            freeCameraResource();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA,PERMISSION_REQ_ID_CAMERA);
                } else {
                    showLongToast("no permission for " + Manifest.permission.RECORD_AUDIO);
                    finish();
                }
                break;
            }
            case PERMISSION_REQ_ID_CAMERA: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    showLongToast("no permission for " + Manifest.permission.CAMERA);
                    finish();
                }
                break;
            }
        }
    }
}
