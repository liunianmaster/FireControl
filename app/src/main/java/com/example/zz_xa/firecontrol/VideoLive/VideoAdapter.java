package com.example.zz_xa.firecontrol.VideoLive;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zz_xa.firecontrol.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adminstrator of wxb on 2018/5/14.
 * Fix by:
 */

public class VideoAdapter extends BaseAdapter {
    private List<VideoPicture> listPictures;

    private HashMap<Integer, View> mView;
    private HashMap<Integer, Integer> visiblecheck;
    private HashMap<Integer, Boolean> ischeck;
    private LayoutInflater inflater = null;
    private Context context;
    private TextView txtcount;

    private static VideoLive mVideo = null;

    public static void setMainActivity(VideoLive activity){
        mVideo = activity;
    }

    public VideoAdapter(List<VideoPicture> listPictures, Context context, TextView txtcount) {
        super();
        this.listPictures = listPictures;
        this.context = context;
        this.txtcount = txtcount;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = new HashMap<Integer, View>();
        visiblecheck = new HashMap<Integer, Integer>();
        ischeck = new HashMap<Integer, Boolean>();
        if(mVideo.isMulChoice){
            for(int i=0; i<listPictures.size(); i++){
                ischeck.put(i, false);
                visiblecheck.put(i, CheckBox.VISIBLE);
            }
        } else {
            for(int i=0; i<listPictures.size(); i++){
                ischeck.put(i, false);
                visiblecheck.put(i, CheckBox.INVISIBLE);
            }
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listPictures.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listPictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View v, ViewGroup arg2) {
        // TODO Auto-generated method stu
        View view = mView.get(position);
        if(view == null) {
            //view = getLayoutInflater().inflate(R.layout.video_item2, null);
            view = inflater.inflate(R.layout.video_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_show);
            TextView textView = (TextView) view.findViewById(R.id.tv_show);

            imageView.setImageBitmap(listPictures.get(position).getBitmap());
            //textView.setText(listPictures.get(position).getPath());
            textView.setText(listPictures.get(position).getFileName());
            ;
            TextView textViewTime = (TextView) view.findViewById(R.id.time_show);
            textViewTime.setText("创建时间：" + listPictures.get(position).getFixTime());

            final CheckBox ceb = (CheckBox) view.findViewById(R.id.check);
            ceb.setChecked(ischeck.get(position));
            ceb.setVisibility(visiblecheck.get(position));
            view.setOnLongClickListener(new VideoAdapter.OnLongClick());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mVideo.isMulChoice){
                        if(ceb.isChecked()){
                            ceb.setChecked(false);
                            mVideo.selectid.remove(listPictures.get(position));
                        }else {
                            ceb.setChecked(true);
                            mVideo.selectid.add(listPictures.get(position));
                        }
                        txtcount.setText("共选择了"+mVideo.selectid.size()+"项");
                    } else {
                        playVideo(listPictures.get(position).getPath());
                        //Toast.makeText(getContext(),"click "+listPictures.get(position), Toast.LENGTH_LONG).show();
                    }
                }
            });

            mView.put(position, view);
        }
        return view;
    }

    class OnLongClick implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {
            mVideo.isMulChoice = true;

            mVideo.selectid.clear();
            mVideo.layout.setVisibility(View.VISIBLE);
            mVideo.imageButton.setVisibility(View.GONE);
            for(int i=0; i<listPictures.size(); i++){
               // mVideo.adapter.visiblecheck.put(i, CheckBox.VISIBLE);
                visiblecheck.put(i, CheckBox.VISIBLE);
            }
            mVideo.adapter = new VideoAdapter(listPictures,context,txtcount);
            mVideo.listView.setAdapter(mVideo.adapter);
            return true;
        }
    }

    //调用系统播放器   播放视频
    private void playVideo(String videoPath){
//					   Intent intent = new Intent(Intent.ACTION_VIEW);
//					   String strend="";
//					   if(videoPath.toLowerCase().endsWith(".mp4")){
//						   strend="mp4";
//					   }
//					   else if(videoPath.toLowerCase().endsWith(".3gp")){
//						   strend="3gp";
//					   }
//					   else if(videoPath.toLowerCase().endsWith(".mov")){
//						   strend="mov";
//					   }
//					   else if(videoPath.toLowerCase().endsWith(".avi")){
//						   strend="avi";
//					   }
//					   intent.setDataAndType(Uri.parse(videoPath), "video/*");
//					   startActivity(intent);

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(videoPath);
        intent.setDataAndType(Uri.fromFile(file), "video/*");
        context.startActivity(intent);
    }
}
