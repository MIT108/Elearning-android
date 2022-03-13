package com.se3.elearning.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.se3.elearning.databinding.ItemContainerSentMessageBinding;
import com.se3.elearning.databinding.ItemContainterReceivedMessageBinding;
import com.se3.elearning.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ChatMessage> chatMessages;
    private final String senderId;
    private  String receiverName;
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public void setReceiverProfileImage(String bitmap){
        receiverName = bitmap;
    }

    public ChatAdapter(List<ChatMessage> chatMessages, String senderId, String receiverName) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
        this.receiverName = receiverName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new ReceivedMessageViewHolder(
                    ItemContainterReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData((chatMessages.get(position)));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiverName);
        }

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }

    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.msg);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainterReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainterReceivedMessageBinding itemContainterReceivedMessageBinding) {
            super(itemContainterReceivedMessageBinding.getRoot());
            binding = itemContainterReceivedMessageBinding;
        }

        void setData(ChatMessage chatMessage, String receiverName) {
            binding.textMessage.setText(chatMessage.msg);
            binding.textDateTime.setText(chatMessage.dateTime);
            if (receiverName != null) {
                binding.imageProfile.setText(receiverName.substring(0, 1));
            }
        }
    }
}
