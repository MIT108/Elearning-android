package com.se3.elearning.adapters;

import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.se3.elearning.R;
import com.se3.elearning.model.VideoModel;

public class VideoAdapter {
//
//    @NonNull
//    @Override
//    public VideoModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull VideoModel holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    class myViewHolder extends RecyclerView.ViewHolder{
//        VideoView videoView;
//        TextView desc, title;
//        ProgressBar pBar;
//
//        public myViewHolder(@NonNull View itemView){
//            super(itemView);
//
//            videoView = (VideoView) itemView.findViewById(R.id.videoView);
//            desc = (TextView) itemView.findViewById(R.id.textVideoDescription);
//            title = (TextView) itemView.findViewById(R.id.textVideoTitle);
//            pBar = (ProgressBar) itemView.findViewById(R.id.videoProgressBar);
//        }
//
//        void setData(VideoModel obj){
//            videoView.setVideoPath(obj.getUrl());
//            title.setText(obj.getName());
//            desc.setText(obj.getDesc());
//
//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    pBar.setVisibility(View.GONE);
//                    mediaPlayer.start();
//                }
//            });
//
//            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    mediaPlayer.start();
//                }
//            });
//        }
//    }
}
