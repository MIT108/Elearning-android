package com.se3.elearning.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.se3.elearning.R;

public class MyViewHolder extends RecyclerView.ViewHolder{


    TextView time, name, url, firstChar;
    ImageView download;

    public MyViewHolder(@NonNull View itemView){
        super(itemView);

        time = itemView.findViewById(R.id.content_time);
        name  = itemView.findViewById(R.id.content_title);
        url = itemView.findViewById(R.id.download_link);
        download = itemView.findViewById(R.id.content_download);
        firstChar = itemView.findViewById(R.id.content_number);



    }

}
