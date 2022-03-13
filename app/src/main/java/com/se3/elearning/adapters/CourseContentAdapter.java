package com.se3.elearning.adapters;


import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.se3.elearning.R;
import com.se3.elearning.model.CourseContent;
import com.se3.elearning.student.course.CoursePage;

import java.util.ArrayList;


public class CourseContentAdapter extends RecyclerView.Adapter<MyViewHolder>{

    CoursePage coursePageActivity;
    ArrayList<CourseContent> courseContents;
    MyViewHolder myViewHolder;

    public CourseContentAdapter(CoursePage coursePageActivity, ArrayList<CourseContent> courseContents){
        this.courseContents = courseContents;
        this.coursePageActivity = coursePageActivity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(coursePageActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.course_list_row_items, null, false);
        myViewHolder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        myViewHolder.name.setText(courseContents.get(i).getName());
        myViewHolder.url.setText(courseContents.get(i).getUrl());
        myViewHolder.time.setText(courseContents.get(i).getTime());
        myViewHolder.firstChar.setText(courseContents.get(i).getName().substring(0, 1));
        myViewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFile(myViewHolder.name.getContext(), courseContents.get(i).getName(), ".pdf", DIRECTORY_DOWNLOADS, courseContents.get(i).getUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return courseContents.size();
    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url){

        DownloadManager downloadManager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName+fileExtension);

        downloadManager.enqueue(request);

    }
}
