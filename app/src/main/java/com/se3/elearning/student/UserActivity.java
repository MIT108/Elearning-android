package com.se3.elearning.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.se3.elearning.R;
import com.se3.elearning.adapters.UsersChatAdapter;
import com.se3.elearning.databinding.ActivityUserBinding;
import com.se3.elearning.listeners.UserChatListener;
import com.se3.elearning.model.User;
import com.se3.elearning.utilities.Constants;
import com.se3.elearning.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends BaseActivity implements UserChatListener {

    private ActivityUserBinding binding;
    private PreferenceManager preferenceManager;
    private UserChatListener userChatListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.imagename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getUsers();
    }

    private void getUsers(){
        loading(true);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        loading(false);
                        String myUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<User> users = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if (myUserId.equals(documentSnapshot.getId())) {
                                    continue;
                                }
                                User user = new User();
                                user.userName = documentSnapshot.getString(Constants.KEY_NAME);
                                user.userEmail = documentSnapshot.getString(Constants.KEY_EMAIL);
                                user.userToken = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                user.id = documentSnapshot.getId();
                                users.add(user);
                            }
                            if (users.size() > 0) {
                                userChatListener = new UserChatListener() {
                                    @Override
                                    public void onUserClicked(User user) {

                                    }
                                };

                                UsersChatAdapter usersChatAdapter = new UsersChatAdapter(users, UserActivity.this);
                                binding.usersChatRecyclerView.setAdapter(usersChatAdapter);
                                binding.usersChatRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                showErrorMessage();
                            }

                        } else {
                            showErrorMessage();
                        }
                    }
                });

    }

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}