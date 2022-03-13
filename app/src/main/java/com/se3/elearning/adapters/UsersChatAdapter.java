package com.se3.elearning.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.se3.elearning.databinding.ItemContainerUserBinding;
import com.se3.elearning.databinding.ListUserChatBinding;
import com.se3.elearning.listeners.UserChatListener;
import com.se3.elearning.model.User;

import java.util.List;

public class UsersChatAdapter extends RecyclerView.Adapter<UsersChatAdapter.UserViewHolder> {

    private final List<User> users;
    private  final UserChatListener userChatListener;
    public UsersChatAdapter(List<User> users, UserChatListener userChatListener){
        this.users = users;
        this.userChatListener =  userChatListener;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListUserChatBinding listUserChatBinding = ListUserChatBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(listUserChatBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersChatAdapter.UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        ListUserChatBinding binding;
        UserViewHolder(ListUserChatBinding listUserChatBinding){
            super(listUserChatBinding.getRoot());
            binding = listUserChatBinding;
        }

        void setUserData(User user){
            binding.textName.setText(user.userName);
            binding.textEmail.setText(user.userEmail);
            binding.textFirstChar.setText(user.userName.substring(0,1));
            binding.getRoot().setOnClickListener(v -> {
                userChatListener.onUserClicked(user);
            });
        }
    }
}
