package com.se3.elearning.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.se3.elearning.databinding.ListUserChatConversationBinding;
import com.se3.elearning.listeners.ConversionListener;
import com.se3.elearning.model.ChatMessage;
import com.se3.elearning.model.User;
import com.se3.elearning.utilities.PreferenceManager;

import java.util.List;

public class RecentConversationsAdapter  extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder>{
    private final List<ChatMessage> chatMessages;

    private final ConversionListener conversionListener;

    public RecentConversationsAdapter(List<ChatMessage> chatMessages, ConversionListener conversionListener) {
        this.chatMessages = chatMessages;
        this.conversionListener = conversionListener;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ListUserChatConversationBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                        ,parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecentConversationsAdapter.ConversionViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    class ConversionViewHolder extends RecyclerView.ViewHolder{
        ListUserChatConversationBinding binding;

        ConversionViewHolder(ListUserChatConversationBinding listUserChatConversationBinding){
            super(listUserChatConversationBinding.getRoot());
            binding = listUserChatConversationBinding;
        }

        void setData(ChatMessage chatMessage){
            binding.textFirstChar.setText(chatMessage.conversionName.substring(0,1));
            binding.textName.setText(chatMessage.conversionName);
            binding.textRecentMessage.setText((chatMessage.msg));
            binding.getRoot().setOnClickListener(v -> {
                User user = new User();
                user.id = chatMessage.conversionId;
                user.userName = chatMessage.conversionName;
                conversionListener.onConversionClicked(user);
            });
        }
    }
}
