package com.example.zz_xa.firecontrol.VideoLive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zz_xa.firecontrol.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ZZ-XA of wxb on 2018/4/12.
 * Fix by:
 */

public class VideoLive extends Fragment {

    private String cur_path="/storage/emulated/0/11/video/";
    private List<VideoPicture> listPictures;
    ListView listView ;

    public boolean isMulChoice = false;
    public RelativeLayout layout;
    public ImageButton imageButton;
    private Button cancle,delete;
    private TextView txtcount;
    //private VideoLive.MyAdapter adapter;
    public VideoAdapter adapter;
    public List<VideoPicture> selectid = new ArrayList<VideoPicture>();
    private Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if (msg.what == 0) {
                List<VideoPicture> listPictures = (List<VideoPicture>) msg.obj;
//				Toast.makeText(getApplicationContext(), "handle"+listPictures.size(), 1000).show();
                //VideoLive.MyAdapter adapter = new VideoLive.MyAdapter(listPictures);
                //adapter = new VideoLive.MyAdapter(listPictures);
               // adapter = new VideoLive.MyAdapter(listPictures,getContext(),txtcount);
                adapter = new VideoAdapter(listPictures,getContext(),txtcount);
                listView.setAdapter(adapter);
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.video_live, container, false);

        imageButton = (ImageButton)v.findViewById(R.id.videolive_imgbtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), VideoLiveActivity.class);
                //Intent intent = new Intent(getActivity(), VideoRecorder.class);
                //Intent intent = new Intent(getActivity(), VideoShoot.class);
                //Intent intent = new Intent(getActivity(), VideoItemClick.class);
                startActivity(intent);
            }
        });

        VideoAdapter.setMainActivity(this);

      //  context = getContext();
        loadVaule(v);

        return v;
    }

    private void loadVaule(View v){
        File file = new File(cur_path);
        File[] files  = null;
        files = file.listFiles();
        listPictures = new ArrayList<VideoPicture>();
        for (int i = 0; i < files.length; i++) {
            VideoPicture picture = new VideoPicture();
            picture.setBitmap(getVideoThumbnail(files[i].getPath(), 200, 200, MediaStore.Images.Thumbnails.MICRO_KIND));
            picture.setPath(files[i].getPath());
            picture.setFileName(files[i].getName());
            long time = files[i].lastModified();
            String ctime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time));
            picture.setFixTime(ctime);

            listPictures.add(picture);
        }
        listView = (ListView) v.findViewById(R.id.lv_show);
    //    listView.setOnItemClickListener(this);
        layout = (RelativeLayout)v.findViewById(R.id.relative);
        txtcount = (TextView)v.findViewById(R.id.txtcount);

        cancle = (Button)v.findViewById(R.id.cancle);
        delete = (Button)v.findViewById(R.id.delete);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMulChoice = false;
                selectid.clear();
                adapter = new VideoAdapter(listPictures,getContext(),txtcount);
                listView.setAdapter(adapter);
                layout.setVisibility(View.GONE);
                imageButton.setVisibility(View.VISIBLE);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMulChoice = false;

                selectid.clear();
                adapter = new VideoAdapter(listPictures,getContext(),txtcount);
                listView.setAdapter(adapter);
                layout.setVisibility(View.GONE);
                imageButton.setVisibility(View.VISIBLE);
            }
        });

        Message msg = new Message();
        msg.what = 0;
        msg.obj = listPictures;

        handler.sendMessage(msg);
    }

    //获取视频的缩略图
    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
//		        System.out.println("w"+bitmap.getWidth());
//		        System.out.println("h"+bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
