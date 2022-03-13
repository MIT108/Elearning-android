package com.se3.elearning.student;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.se3.elearning.MainActivity;
import com.se3.elearning.R;
import com.se3.elearning.adapters.ChatAdapter;
import com.se3.elearning.adapters.GroupChatAdapter;
import com.se3.elearning.model.ChatMessage;
import com.se3.elearning.model.GroupChatMessage;
import com.se3.elearning.model.User;
import com.se3.elearning.student.other.Utils;
import com.se3.elearning.student.social.OutgoingInvitationActivity;
import com.se3.elearning.student.social.StudentCall;
import com.se3.elearning.student.social.StudentChat;
import com.se3.elearning.student.social.StudentReel;
import com.se3.elearning.student.social.VPAdapter;
import com.se3.elearning.utilities.Constants;
import com.se3.elearning.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class StudentLiveChat extends Fragment {
    PreferenceManager preferenceManager;
    ImageView globalMeeting;
    private List<User> selectedUsers;
    private List<User> users;

    public StudentLiveChat(){
        selectedUsers = new ArrayList<>();
    }

    public List<User> getSelectedUsers(){
        return selectedUsers;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_student_live_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceManager = new PreferenceManager(getContext());

        getUsers();

        globalMeeting = view.findViewById(R.id.imageVideoMeeting);

        users = new ArrayList<>();


        if (preferenceManager.getString(Constants.KEY_ROLE).equals("0")){
            globalMeeting.setVisibility(View.VISIBLE);
        }else{
            globalMeeting.setVisibility(View.GONE);
        }

        globalMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OutgoingInvitationActivity.class);
                intent.putExtra("selectedUsers", new Gson().toJson(users));
                intent.putExtra("type", "video");
                intent.putExtra("isMultiple", true);
                startActivity(intent);

            }
        });


    }

    private void getUsers() {
        System.out.println("class id is : "+preferenceManager.getString(Constants.KEY_CLASSID));
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String myUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                        if (task.isSuccessful() && task.getResult() != null) {
                            users.clear();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if (myUserId.equals(documentSnapshot.getId())) {
                                    continue;
                                }
                                User user = new User();
                                user.userName = documentSnapshot.getString(Constants.KEY_NAME);
                                user.userEmail = documentSnapshot.getString(Constants.KEY_EMAIL);
                                user.userToken = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                users.add(user);
                            }
                            if (users.size() == 0) {
                                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                                Utils.positiveAlertMessage("No user available in this course", getContext());
                            }

                        } else {
                            Utils.positiveAlertMessage("No user available in this course", getContext());
                        }
                    }
                });
    }


}