package com.se3.elearning.student.social;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.se3.elearning.R;
import com.se3.elearning.adapters.UsersVideoAdapter;
import com.se3.elearning.utilities.PreferenceManager;

import java.util.ArrayList;

public class StudentReel extends Fragment {

    ViewPager2 vPager;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_reel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView)view.findViewById(R.id.addVideo);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UploadVideoActivity.class));

            }
        });

        vPager = (ViewPager2) view.findViewById(R.id.vPager);


    }
}