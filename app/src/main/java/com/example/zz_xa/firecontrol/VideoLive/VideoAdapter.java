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
 * Created by ZZ-XA on 2018/5/14.
 */

public class VideoAdapter extends BaseAdapter {
    private List<VideoPicture> mListPictures;

    private HashMap<Integer, View> mView;
    private HashMap<Integer, Integer> mVisibleCheck;
    private HashMap<Integer, Boolean> mIsCheck;
    private LayoutInflater mInflater = null;
    private Context mContext;
    private TextView mTxtCount;

    private static VideoLive mVideoLive = null;

    public static void setMainActivity(VideoLive activity) {
        mVideoLive = activity;
    }
    public VideoAdapter(List<VideoPicture> listPictures, Context context, TextView mTxtCount){
        super();
        this.mListPictures = listPictures;
        this.mContext = context;
        this.mTxtCount = mTxtCount;

        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = new HashMap<Integer, View>();
        mVisibleCheck = new HashMap<Integer, Integer>();
        mIsCheck = new HashMap<Integer, Boolean>();

        if (mVideoLive.isMulChoice){
            for (int i=0; i<mListPictures.size(); i++){
                mIsCheck.put(i, false);
                mVisibleCheck.put(i, CheckBox.VISIBLE);
            }
        } else {
            for (int i=0; i<mListPictures.size(); i++){
                mIsCheck.put(i, false);
                mVisibleCheck.put(i, CheckBox.INVISIBLE);
            }
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = mView.get(position);
        if(view == null){
            view = mInflater.inflate(R.layout.video_adapter, null);
            ImageView imageView = (ImageView)view.findViewById(R.id.iv_show);
            TextView textView = (TextView)view.findViewById(R.id.tv_show);

            imageView.setImageBitmap(mListPictures.get(position).getBitmao());
            textView.setText(mListPictures.get(position).getFileName());

            TextView textViewTime = (TextView)view.findViewById(R.id.time_show);
            textViewTime.setText("创建时间：" + mListPictures.get(position).getFileTime());

            final CheckBox ceb = (CheckBox)view.findViewById(R.id.check);
            ceb.setChecked(mIsCheck.get(position));
            ceb.setVisibility(mVisibleCheck.get(position));
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mVideoLive.isMulChoice = true;
                    mVideoLive.selectid.clear();
                    mVideoLive.layout.setVisibility(View.VISIBLE);
                    mVideoLive.imageButton.setVisibility(View.GONE);
                    for(int i=0; i<mListPictures.size(); i++){
                        //mVideoLive.adapter.mVisibleCheck.put(i, CheckBox.VISIBLE);
                        mVisibleCheck.put(i, CheckBox.VISIBLE);
                    }
                    mVideoLive.adapter = new VideoAdapter(mListPictures,mContext,mTxtCount);
                    mVideoLive.listView.setAdapter(mVideoLive.adapter);
                    return true;
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mVideoLive.isMulChoice){
                        if(ceb.isChecked()){
                            ceb.setChecked(false);
                            mVideoLive.selectid.remove(mListPictures.get(position));

                        } else {
                            ceb.setChecked(true);
                            mVideoLive.selectid.add(mListPictures.get(position));
                        }
                        mTxtCount.setText("共选择了"+mVideoLive.selectid.size()+"项");
                    } else {
                        playVideo(mListPictures.get(position).getPath());
                    }
                }
            });
            mView.put(position, view);
        }
        return view;
    }

    private void playVideo(String videoPath){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(videoPath);
        intent.setDataAndType(Uri.fromFile(file), "video/*");

        mContext.startActivity(intent);
    }


    @Override
    public int getCount() {
        return mListPictures.size();
    }

    @Override
    public Object getItem(int position) {
        return mListPictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
