package com.se3.elearning.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.se3.elearning.databinding.ItemContainerGroupReceivedMessageBinding;
import com.se3.elearning.databinding.ItemContainerGroupSentMessageBinding;
import com.se3.elearning.databinding.ItemContainerSentMessageBinding;
import com.se3.elearning.databinding.ItemContainterReceivedMessageBinding;
import com.se3.elearning.model.ChatMessage;
import com.se3.elearning.model.GroupChatMessage;

import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<GroupChatMessage> groupChatMessages;
    private final String senderId;
    private final String receiverName;
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public GroupChatAdapter(List<GroupChatMessage> groupChatMessages, String senderId, String receiverName) {
        this.groupChatMessages = groupChatMessages;
        this.senderId = senderId;
        this.receiverName = receiverName;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT){
            return new GroupChatAdapter.SentGroupMessageViewHolder(
                    ItemContainerGroupSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }else {
            return new GroupChatAdapter.ReceivedGroupMessageViewHolder(
                    ItemContainerGroupReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT){
            ((GroupChatAdapter.SentGroupMessageViewHolder) holder).setData((groupChatMessages.get(position)));
        }else{
            ((GroupChatAdapter.ReceivedGroupMessageViewHolder) holder).setData(groupChatMessages.get(position), receiverName);
        }

    }

    @Override
    public int getItemCount() {
        return groupChatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        System.out.println(groupChatMessages.get(position).dateTime);
        System.out.println(groupChatMessages.get(position).msg);
        System.out.println(groupChatMessages.get(position).senderId);
        System.out.println(groupChatMessages.get(position).receiverId);
        if (groupChatMessages.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        }else{
            return VIEW_TYPE_RECEIVED;
        }

    }

    static class SentGroupMessageViewHolder extends  RecyclerView.ViewHolder{
        private final ItemContainerGroupSentMessageBinding binding;

        SentGroupMessageViewHolder(ItemContainerGroupSentMessageBinding itemContainerGroupSentMessageBinding){
            super(itemContainerGroupSentMessageBinding.getRoot());
            binding = itemContainerGroupSentMessageBinding;
        }

        void setData(GroupChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.msg);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedGroupMessageViewHolder extends  RecyclerView.ViewHolder{
        private final ItemContainerGroupReceivedMessageBinding binding;

        ReceivedGroupMessageViewHolder(ItemContainerGroupReceivedMessageBinding itemContainerGroupReceivedMessageBinding){
            super(itemContainerGroupReceivedMessageBinding.getRoot());
            binding = itemContainerGroupReceivedMessageBinding;
        }

        void setData(GroupChatMessage chatMessage, String receiverName){
            binding.textMessage.setText(chatMessage.msg);
            binding.textDateTime.setText(chatMessage.dateTime);
            binding.textName.setText(chatMessage.senderName);
            binding.imageProfiles.setText(chatMessage.FN);
        }
    }
}
