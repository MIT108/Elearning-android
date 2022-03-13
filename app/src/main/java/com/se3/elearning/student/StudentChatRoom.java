package com.se3.elearning.student;


import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.se3.elearning.R;
import com.se3.elearning.adapters.GroupChatAdapter;
import com.se3.elearning.model.GroupChatMessage;
import com.se3.elearning.model.User;
import com.se3.elearning.student.other.Message;
import com.se3.elearning.student.other.Utils;
import com.se3.elearning.student.other.WsConfig;
import com.se3.elearning.utilities.Constants;
import com.se3.elearning.utilities.PreferenceManager;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StudentChatRoom extends Fragment
{
    private User receiverUser;
    private List<GroupChatMessage> groupChatMessages;
    private GroupChatAdapter groupChatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private RecyclerView recyclerView;
    private TextView inputMessage;
    private FrameLayout sendBtn;
    private ProgressBar progressBar;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_student_chat_room, container, false);

        return view;
    }

    @Override
    public void  onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        preferenceManager = new PreferenceManager(getContext());
        groupChatMessages = new ArrayList<>();
        receiverUser = new User();

        sendBtn = view.findViewById(R.id.layoutSend);
        inputMessage = view.findViewById(R.id.inputMessage);
        progressBar = view.findViewById(R.id.progressBar);

        groupChatAdapter = new GroupChatAdapter(
                groupChatMessages,
                preferenceManager.getString(Constants.KEY_USER_ID),
                receiverUser.userName
        );
        recyclerView = view.findViewById(R.id.chatRecyclerView);
        recyclerView.setAdapter(groupChatAdapter);
        database = FirebaseFirestore.getInstance();

        listenMessages();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(inputMessage.getText().toString());
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_GROUP_ID, preferenceManager.getString(Constants.KEY_COURSEID));
        message.put(Constants.KEY_MESSAGE, inputMessage.getText().toString());
        message.put(Constants.KEY_USER_NAME, preferenceManager.getString(Constants.KEY_USER_NAME));
        message.put(Constants.KEY_FN, preferenceManager.getString(Constants.KEY_FN));
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_GROUP_CHAT).add(message);
        inputMessage.setText(null);
    }

    private void listenMessages() {
        database.collection(Constants.KEY_COLLECTION_GROUP_CHAT)
                .whereEqualTo(Constants.KEY_RECEIVER_GROUP_ID, preferenceManager.getString(Constants.KEY_COURSEID))
                .addSnapshotListener(eventListener);

    }


    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = groupChatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    GroupChatMessage groupChatMessage = new GroupChatMessage();
                    System.out.println("the sender id is : "+documentChange.getDocument().getString(Constants.KEY_SENDER_ID)+" and "+Constants.KEY_SENDER_ID);
                    groupChatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    groupChatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_GROUP_ID);
                    groupChatMessage.msg = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    groupChatMessage.senderName = documentChange.getDocument().getString(Constants.KEY_USER_NAME);
                    groupChatMessage.FN = documentChange.getDocument().getString(Constants.KEY_FN);
                    groupChatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    groupChatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
//                    System.out.println(groupChatMessage.senderId+" other thing");
                    groupChatMessages.add(groupChatMessage);
                }
            }
            Collections.sort(groupChatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0) {
                groupChatAdapter.notifyDataSetChanged();
            } else {
                groupChatAdapter.notifyItemRangeChanged(groupChatMessages.size(), groupChatMessages.size());
                recyclerView.smoothScrollToPosition(groupChatMessages.size() - 1);
            }
            recyclerView.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    };



    public String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }



}