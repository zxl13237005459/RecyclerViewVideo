package com.example.administrator.recyclerviewvideo;

/**
 * Created by Administrator on 2016/6/23.
 */
public class VideoItem{
    private String p360;
    private String cover;
    private int width;
    private int height;
    private String title;

    public VideoItem(){
    }

    public VideoItem(String p360, String cover, int width, int height, String title){
        this.p360 = p360;
        this.cover = cover;
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public String getP360(){
        return p360;
    }

    public void setP360(String p360){
        this.p360 = p360;
    }

    public String getCover(){
        return cover;
    }

    public void setCover(String cover){
        this.cover = cover;
    }

    public int getWidth(){
        return width;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
