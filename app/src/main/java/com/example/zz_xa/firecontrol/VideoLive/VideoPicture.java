package com.example.zz_xa.firecontrol.VideoLive;

import android.graphics.Bitmap;

/**
 * Created by ZZ-XA on 2018/5/14.
 */

public class VideoPicture {
    private Bitmap bitmap;
    private String path;
    private String fileName;
    private String fileTime;

    public Bitmap getBitmao(){
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public String getPath(){
        return path;
    }
    public void setPath(String path){
        this.path = path;
    }
    public String getFileName(){
        return fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public String getFileTime(){
        return fileTime;
    }
    public void setFileTime(String fileTime){
        this.fileTime = fileTime;
    }

    public VideoPicture(){
        super();
    }
    public VideoPicture(Bitmap bitmap, String path, String fileName, String fileTime){
        this.bitmap = bitmap;
        this.path = path;
        this.fileName = fileName;
        this.fileTime = fileTime;
    }

}
