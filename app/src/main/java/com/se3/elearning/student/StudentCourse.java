package com.se3.elearning.student;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.se3.elearning.R;
import com.se3.elearning.listeners.UsersListener;
import com.se3.elearning.model.User;
import com.se3.elearning.student.social.StudentCall;
import com.se3.elearning.student.social.StudentChat;
import com.se3.elearning.student.social.StudentReel;
import com.se3.elearning.student.social.VPAdapter;

public class StudentCourse extends Fragment{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_course, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);



        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new StudentChat(), "CHAT");
        vpAdapter.addFragment(new StudentReel(), "REEL");
        vpAdapter.addFragment(new StudentCall(), "CALL");
        viewPager.setAdapter(vpAdapter);

    }
}