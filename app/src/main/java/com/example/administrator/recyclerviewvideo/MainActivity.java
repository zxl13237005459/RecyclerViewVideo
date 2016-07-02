package com.example.administrator.recyclerviewvideo;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private List<VideoItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        MyAdapter adapter = new MyAdapter(this,list,linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
        });

    }

    private void initData(){
        list= new ArrayList<>();
        String json = getStringJson();
        //解析数据
        try{
            JSONArray jsonArray = new JSONObject(json).getJSONObject("data").getJSONArray("data");
            for(int i = 0; i <jsonArray.length() ; i++){
                try{
                    JSONObject data = jsonArray.getJSONObject(i);
                    JSONObject group = data.getJSONObject("group");
                    JSONObject p360 = group.getJSONObject("360p_video");
                    String p360Url = p360.getJSONArray("url_list").getJSONObject(0).getString("url");
                    JSONObject medium_cover = group.getJSONObject("medium_cover");
                    JSONArray url_list = medium_cover.getJSONArray("url_list");
                    String coverUrl = url_list.getJSONObject(0).getString("url");
                    //JSONObject medium_cover = group.getJSONObject("medium_cover");
                    //JSONArray url_list = medium_cover.getJSONArray("url_list");
                    int video_width = group.getInt("video_width");
                    int video_height = group.getInt("video_height");
                    String title = group.getString("title");
                    VideoItem videoItem = new VideoItem(p360Url,coverUrl,video_width,video_height,title);

                    list.add(videoItem);
                }catch(JSONException e){
                    //  e.printStackTrace();
                }
                // Log.d("MainActivity", "list.size():" + list.size());
            }
        }catch(JSONException e){
            e.printStackTrace();
            Log.d("MainActivity", "aa");
        }
    }

    public String getStringJson(){
        AssetManager assets = getAssets();
        BufferedReader br = null;
        StringBuffer sb = null;
        try{
            InputStream is = assets.open("video1.json");
            br = new BufferedReader(new InputStreamReader(is));
            sb = new StringBuffer();
            String line = "";
            while((line=br.readLine())!=null){
                sb.append(line);
            }
            return sb.toString();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(br!=null){
                try{
                    br.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
