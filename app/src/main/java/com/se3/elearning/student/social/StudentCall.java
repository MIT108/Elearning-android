package com.se3.elearning.student.social;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.se3.elearning.R;
import com.se3.elearning.adapters.UsersVideoAdapter;
import com.se3.elearning.listeners.UsersListener;
import com.se3.elearning.model.User;
import com.se3.elearning.student.other.Utils;
import com.se3.elearning.utilities.Constants;
import com.se3.elearning.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class StudentCall extends Fragment implements UsersListener {
    private List<User> users;
    private UsersVideoAdapter userAdapter;
    private TextView textErrorMessage;
    private PreferenceManager preferenceManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton imageConference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView userRecyclerView = view.findViewById(R.id.userRecyclerView);

        textErrorMessage = view.findViewById(R.id.textErrorMessage);
        preferenceManager = new PreferenceManager(getActivity());
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::getUsers);
        imageConference = view.findViewById(R.id.imageConference);

        users = new ArrayList<>();
        userAdapter = new UsersVideoAdapter(users, this);
        userRecyclerView.setAdapter(userAdapter);

        getUsers();

    }


    private void getUsers() {
        swipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        swipeRefreshLayout.setRefreshing(false);
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
                            if (users.size() > 0) {
                                userAdapter.notifyDataSetChanged();
                            } else {
                                textErrorMessage.setText(String.format("%s", "No user available"));
                                textErrorMessage.setVisibility(View.VISIBLE);
                            }

                        } else {
                            textErrorMessage.setText(String.format("%s", "No user available"));
                            textErrorMessage.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void initiateVideoMeeting(User user) {
        if (user.userToken == null || user.userToken.trim().isEmpty()){
            Utils.negativeAlertMessage(user.userName+" is not available for meeting", getContext());
        }else{
            Intent intent = new Intent(getContext(), OutgoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "video");
            startActivity(intent);
        }
    }

    @Override
    public void initiateAudioMeeting(User user) {
        if (user.userToken == null || user.userToken.trim().isEmpty()){
            Utils.negativeAlertMessage(user.userName+" is not available for meeting", getContext());
        }else{
            Intent intent = new Intent(getContext(), OutgoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "audio");
            startActivity(intent);
        }
    }

    @Override
    public void onMultipleUsersAction(Boolean isMultipleUsersSelected) {
        if (isMultipleUsersSelected){
            imageConference.setVisibility(View.VISIBLE);
            imageConference.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), OutgoingInvitationActivity.class);
                    intent.putExtra("selectedUsers", new Gson().toJson(userAdapter.getSelectedUsers()));
                    intent.putExtra("type", "video");
                    intent.putExtra("isMultiple", true);
                    startActivity(intent);
                }
            });
        }else{
            imageConference.setVisibility(View.GONE);
        }
    }
}