package com.example.zz_xa.firecontrol.VideoLive;

import android.graphics.Bitmap;

/**
 * Created by Adminstrator of wxb on 2018/5/10.
 * Fix by:
 */

public class VideoPicture {
    private Bitmap bitmap;
    private String path;
    private String fileName;
    private String fixTime;

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getFileName(){
        return fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public String getFixTime(){
        return fixTime;
    }
    public void setFixTime(String fixTime){
        this.fixTime = fixTime;
    }
    public VideoPicture(Bitmap bitmap, String path, String fileName, String fixTime) {
        super();
        this.bitmap = bitmap;
        this.path = path;
        this.fileName = fileName;
        this.fixTime = fixTime;
    }
    public VideoPicture() {
        super();
        // TODO Auto-generated constructor stub
    }
}
