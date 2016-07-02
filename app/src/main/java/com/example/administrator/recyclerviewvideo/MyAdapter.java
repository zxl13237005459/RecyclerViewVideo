package com.example.administrator.recyclerviewvideo;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class MyAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<VideoItem> list;
    private LayoutInflater inflater;
    private MediaPlayer mp;
    private int currentPositison = -1;

    private boolean iscreate;
    private MyViewHolder myViewHolder;
    private LinearLayoutManager linearLayoutManager;

    public MyAdapter(Context context, List<VideoItem> list, LinearLayoutManager linearLayoutManager){
        this.context = context;
        this.list = list;
        this.linearLayoutManager = linearLayoutManager;
        inflater = LayoutInflater.from(context);
        mp = new MediaPlayer();
        //准备监听
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp){
                mp.start();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = inflater.inflate(R.layout.recycle_item, parent, false);
        myViewHolder = new MyViewHolder(itemView);
     //   RecyclerView recyclerView = (RecyclerView) parent;
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position){
        VideoItem videoItem = list.get(position);
        //根据服务端返回的数据，重新定义SurfaceView和ImageView的宽和高
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ((MyViewHolder) holder).surfaceView.getLayoutParams();
        params.width = videoItem.getWidth();
        params.height = videoItem.getHeight();
        //下面两行代码功能一样
        ((MyViewHolder) holder).surfaceView.requestLayout();
        //((MyViewHolder) holder).surfaceView.setLayoutParams(params);
        ((MyViewHolder) holder).imageView.setLayoutParams(params);
        //布局文本
        ((MyViewHolder) holder).tvTitle.setText(videoItem.getTitle());
        //添加缩略图图片
        Picasso.with(context).load(videoItem.getCover()).into(((MyViewHolder) holder).imageView);
        //得到当前页首尾的位置 当播放的视频移除视线后停止
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        if(currentPositison != -1 &&mp.isPlaying()){
            if(firstVisibleItemPosition > currentPositison || lastVisibleItemPosition < currentPositison){
                mp.stop();
                currentPositison=-1;
                Log.d("MyAdapter", "stop");
                Log.d("MyAdapter", "currentPositison"+currentPositison);
               // ((MyViewHolder) holder).imageView.setVisibility(View.VISIBLE);
            }
        }else {
            ((MyViewHolder) holder).imageView.setVisibility(View.VISIBLE);
            Log.d("MyAdapter", "stop1");
        }
        

        //添加视频
        if(currentPositison == position){
            Log.d("MyAdapter", "currentPositison"+currentPositison);
            //让视频播放显示
            ((MyViewHolder) holder).surfaceView.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).imageView.setVisibility(View.INVISIBLE);
            final SurfaceHolder holder1 = ((MyViewHolder) holder).surfaceView.getHolder();
           final Uri uri =Uri.parse(videoItem.getP360());
            holder1.addCallback(new SurfaceHolder.Callback(){
                @Override
                public void surfaceCreated(SurfaceHolder holder){
                    Log.d("MyAdapter", "bb");
                    if(currentPositison!=-1){
                        mp.setDisplay(holder1);
                        //重置mp
                        mp.reset();
                        //声明一个SurfaceHolder并添加到mp中
                        //给mp设置播放的视频
                        try{

                            mp.setDataSource(context,uri);
                            // mp.setDataSource(videoItem.getP360());
                            mp.prepareAsync();
                            Log.d("MyAdapter", "ab");
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder){
                }
            });

        }else{
            ((MyViewHolder) holder).imageView.setVisibility(View.VISIBLE);
            //注意，surfaceView的隐藏使用View.INVISIBLE，从而避免SurfaceView被销毁
            ((MyViewHolder) holder).surfaceView.setVisibility(View.INVISIBLE);
        }
        ((MyViewHolder) holder).imageView.setTag(position);
        //设计((MyViewHolder) holder).imageView的点击监听事件 点击的时候开始播放音乐
        ((MyViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //把点击的position赋值给currentPositison，刷新页面的时候就可以播放音乐
                currentPositison = (int) ((MyViewHolder) holder).imageView.getTag();
                //如果有别的视频在播放先关闭
                if(mp != null && mp.isPlaying()){
                    mp.stop();
                    Log.d("MyAdapter", "stop");
                    currentPositison=-1;
                }
                //提醒系统刷新页面
                notifyDataSetChanged();
            }
        });
        //设计((MyViewHolder) holder).surfaceView的点击监听事件 点击的时候停止播放音乐
        ((MyViewHolder) holder).surfaceView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mp != null && mp.isPlaying()){
                    mp.pause();
                }else{
                    mp.start();
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        SurfaceView surfaceView;
        ImageView imageView;
        TextView tvTitle;

        public MyViewHolder(View itemView){
            super(itemView);
            surfaceView = (SurfaceView) itemView.findViewById(R.id.surface);
            imageView = (ImageView) itemView.findViewById(R.id.iv_conver);
            tvTitle = (TextView) itemView.findViewById(R.id.titletv);
        }
    }
}
