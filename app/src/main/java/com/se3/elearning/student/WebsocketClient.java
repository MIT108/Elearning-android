//package com.se3.elearning.student;
//
//import android.annotation.SuppressLint;
//import android.app.KeyguardManager;
//import android.content.Context;
//import android.media.Ringtone;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.cardview.widget.CardView;
//import androidx.fragment.app.Fragment;
//
//import android.os.TokenWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.HttpHeaderParser;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.se3.elearning.MainActivity;
//import com.se3.elearning.R;
//import com.se3.elearning.student.other.Message;
//import com.se3.elearning.student.other.Utils;
//import com.se3.elearning.student.other.WsConfig;
//
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Text;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URI;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.List;
//
//import kotlin.OverloadResolutionByLambdaReturnType;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link StudentChatRoom#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class StudentChatRoomu extends Fragment
//{
//    RelativeLayout relativeLayout;
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private ImageButton btnSend;
//    private EditText inputMsg;
//
//    private WebSocketClient client;
//
//    // Chat messages list adapter
//    private List<Message> listMessages;
//    private ListView listViewMessages;
//
//    private Utils utils;
//
//    // Client name
//    private String name = null;
//    private String message;
////    private MessageAdapter adapter;
//
//    int [] senderId;
//    String [] msg;
//    int [] msgId;
//    JSONArray messages;
//    int lastPosition;
//    private int UserId = 1;
//    private int LastId = 1;
//    MyAdapter adapter;
//
//    // JSON flags to identify the kind of JSON response
//    private static final String TAG_SELF = "self", TAG_NEW = "new",
//            TAG_MESSAGE = "message", TAG_EXIT = "exit";
//
//    public StudentChatRoom() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment StudentChatRoom.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static StudentChatRoom newInstance(String param1, String param2) {
//        StudentChatRoom fragment = new StudentChatRoom();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
////
////    @Override
//
//    @NonNull
//    @Override
//    public  View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
//        View view = inflater.inflate(R.layout.fragment_student_chat_room, container, false);
//        return view;
//    }
//
//    @Override
//    public void  onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState){
//        super.onViewCreated(view, savedInstanceState);
//
//        getMessages("1");
//
////        String[] city = {"fasdf", "douala", "yaounde"};
////
////        ListView listView = (ListView) view.findViewById(R.id.listView);
////        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, city);
////
////        listView.setAdapter(adapter);
////        listView.setOnItemClickListener(this);
//
//        btnSend = (ImageButton) view.findViewById(R.id.btnSend);
//        inputMsg = (EditText) view.findViewById(R.id.inputMsg);
//        listViewMessages = (ListView) view.findViewById(R.id.list_view_messages);
//
//        utils = new Utils(getActivity().getApplicationContext());
//
//        // Getting the person name from previous screen
////        Intent i = getIntent();
////        name = i.getStringExtra("name");
//
//        String name = "Thierry";
//        btnSend.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (inputMsg.getText().toString().isEmpty()){
//                    Toast.makeText(getActivity(), "Enter Message", Toast.LENGTH_SHORT).show();
//                }else{
//                    if (sendMessageToServer(inputMsg.getText().toString(), 1, 1)){
//                        // Sending message to web socket server
//                        String str = utils.getSendMessageJSON(inputMsg.getText().toString());
//                        sendMessageToServer(str);
//                        Log.d("JWT", str);
//                        msg[0] = inputMsg.getText().toString();
//                        senderId[0] = 1;
//                        msgId[0] = 1;
//                        adapter = new MyAdapter(getActivity(), msg, senderId, msgId);
//                        listViewMessages.setAdapter(adapter);
//
//                        // Clearing the input filed once message was sent
//                        inputMsg.setText("");
//                    }
//
//                }
//            }
//        });
//        listMessages = new ArrayList<Message>();
//
////        adapter= new MessageAdapter(getActivity(), listMessages);
////        listViewMessages.setAdapter(adapter);
//
//        /**
//         * Creating web socket client. This will have callback methods
//         * */
//        URI uri = URI.create(WsConfig.URL_WEBSOCKET+ URLEncoder.encode(name));
//        client = new WebSocketClient(uri) {
//
//            @Override
//            public void onOpen(ServerHandshake handshakedata) {
//
//            }
//
//            /**
//             * On receiving the message from web socket server
//             * */
//            @Override
//            public void onMessage(String message) {
////                Log.d("JWebSClientService", message);
//                parseMessage(message);
//
//            }
//
//            /**
//             * Called when the connection is terminated
//             * */
//            @Override
//            public void onClose(int code, String reason, boolean remote) {
//                Log.e("JWebSocketClient", "onClose()");
//            }
//
//            @Override
//            public void onError(Exception error) {
//                Log.e("TAG", "Error! : " + error);
//
////                showToast("Error! : " + error);
////                Toast.makeText(getActivity().getApplicationContext(), "Error! : " + error, Toast.LENGTH_SHORT).show();
//            }
//
//        };
//        try {
//            Log.d("JWebSClientService", "entered");
//            client.connectBlocking();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        if (client != null && client.isOpen()) {
//            client.send("hello");
//            client.send("hello");
//        }else{
//            Log.d("JWebSClientService", "error");
//        }
//
//
//    }/**
// * Method to send message to web socket server
// * */
//private void sendMessageToServer(String message) {
//    if (client != null && client.isOpen()) {
//        client.send(message);
//    }
//}
//
//
//    /**
//     * Parsing the JSON message received from server The intent of message will
//     * be identified by JSON node 'flag'. flag = self, message belongs to the
//     * person. flag = new, a new person joined the conversation. flag = message,
//     * a new message received from server. flag = exit, somebody left the
//     * conversation.
//     * */
//    private void parseMessage(final String msg) {
//
//        try {
//            JSONObject jObj = new JSONObject(msg);
//
//            // JSON node 'flag'
//            String flag = jObj.getString("flag");
//
//            // if flag is 'self', this JSON contains session id
//            if (flag.equalsIgnoreCase(TAG_SELF)) {
//
//                String sessionId = jObj.getString("sessionId");
//
//                // Save the session id in shared preferences
//                utils.storeSessionId(sessionId);
//
//                Log.e("TAG", "Your session id: " + utils.getSessionId());
//
//            } else if (flag.equalsIgnoreCase(TAG_NEW)) {
//                // If the flag is 'new', new person joined the room
//                String name = jObj.getString("name");
//                String message = jObj.getString("message");
//
//                // number of people online
//                String onlineCount = jObj.getString("onlineCount");
//
////                showToast(name + message + ". Currently " + onlineCount
////                        + " people online!");
//
////                Toast.makeText(getActivity().getApplicationContext(),name + message + ". Currently " + onlineCount
////                        + " people online!" , Toast.LENGTH_SHORT).show();
//
//            } else if (flag.equalsIgnoreCase(TAG_MESSAGE)) {
//                // if the flag is 'message', new message received
//                String fromName = name;
//                String message = jObj.getString("message");
//                String sessionId = jObj.getString("sessionId");
//                boolean isSelf = true;
//
//                // Checking if the message was sent by you
//                if (!sessionId.equals(utils.getSessionId())) {
//                    fromName = jObj.getString("name");
//                    isSelf = false;
//                }
//
//                Message m = new Message(fromName, message, isSelf);
//
//                // Appending the message to chat list
//                appendMessage(m);
//
//            } else if (flag.equalsIgnoreCase(TAG_EXIT)) {
//                // If the flag is 'exit', somebody left the conversation
//                String name = jObj.getString("name");
//                String message = jObj.getString("message");
//
////                showToast(name + message);
//
////                Toast.makeText(getActivity().getApplicationContext(),name + message , Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//    //    @Override
//    protected void onClose() {
//        super.onDestroy();
//
//        if(client != null & client.isOpen()){
//            client.close();
//        }
//    }
//
//    /**
//     * Appending message to list view
//     * */
//    private void appendMessage(final Message m) {
//        getActivity().runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                listMessages.add(m);
//                adapter.notifyDataSetChanged();
//
//                // Playing device's notification
//                playBeep();
//            }
//        });
//    }
//    //    private void showToast(final String message) {
////
////        getActivity().runOnUiThread(new Runnable() {
////
////            @Override
////            public void run() {
////                Toast.makeText(getActivity().getApplicationContext(), message,
////                        Toast.LENGTH_LONG).show();
////            }
////        });
////
////    }
//    public void playBeep() {
//
//        try {
//            Uri notification = RingtoneManager
//                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(),
//                    notification);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
//
//    public static String bytesToHex(byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars);
//    }
//
//
////    @Override
////    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////        if(position==0){
////            Toast.makeText(getActivity(), "fasdf", Toast.LENGTH_SHORT).show();
////        }
////        if(position==1){
////            Toast.makeText(getActivity(), "douala", Toast.LENGTH_SHORT).show();
////        }
////        if(position==2){
////            Toast.makeText(getActivity(), "yaounde", Toast.LENGTH_SHORT).show();
////        }
////
////    }
//
////    @Override
////    public void onClick(View view) {
////
//////    }
//////
////    class MessageAdapter extends BaseAdapter {
////        private Context context;
////        private List<Message> messagesItems;
////
////        public MessageAdapter(Context context, List<Message> navDrawerItems) {
////            this.context = context;
////            this.messagesItems = navDrawerItems;
////        }
////
////        @Override
////        public int getCount() {
////            return messagesItems.size();
////        }
////
////        @Override
////        public Object getItem(int position) {
////            return messagesItems.get(position);
////        }
////
////        @Override
////        public long getItemId(int position) {
////            return position;
////        }
////
////        @SuppressLint("InflateParams")
////        @Override
////        public View getView(int position, View convertView, ViewGroup parent) {
////
////            /**
////             * The following list not implemented reusable list items as list items
////             * are showing incorrect data Add the solution if you have one
////             * */
////
////            Message m = messagesItems.get(position);
////
////            LayoutInflater mInflater = (LayoutInflater) context
////                    .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
////
////            // Identifying the message owner
////            if (messagesItems.get(position).isSelf()) {
////                // message belongs to you, so load the right aligned layout
////                convertView = mInflater.inflate(R.layout.list_item_message_right,
////                        null);
////            } else {
////                // message belongs to other person, load the left aligned layout
////                convertView = mInflater.inflate(R.layout.list_item_message_left,
////                        null);
////            }
////
////            TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
////            TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
////
////            txtMsg.setText(m.getMessage());
////            lblFrom.setText(m.getFromName());
////
////            return convertView;
////        }
////    }
//
////    class MyAdapter extends ArrayAdapter<String>{
////        Context context;
////        String M[][];
////
////        MyAdapter(Context c, String msg[][]){
////            super(c, R.layout.list_item_message_left, R.layout.list_item_message_right, R.id.txtMsg, msg);
////
////        }
////    }
//
//    public void getMessages(String sub){
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, WsConfig.Base_URL + "/message/listMessage/" + sub,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            messages = new JSONArray(response);
//
//
//                            senderId = new int[messages.length()];
//                            msg = new String[messages.length()];
//                            msgId = new int[messages.length()];
//
//                            for (int i = 0; i < messages.length(); i++) {
//                                senderId[i] = messages.getJSONObject(i).getInt("userid");
//                                msg[i] = messages.getJSONObject(i).getString("msg");
//                                msgId[i] = messages.getJSONObject(i).getInt("messageid");
//
//                                System.out.println("Message : " + messages.getJSONObject(i).getString("msg"));
//                            }
//                            adapter = new MyAdapter(getActivity(), msg, senderId, msgId);
//                            listViewMessages.setAdapter(adapter);
//                        } catch (JSONException e) {
//                            System.out.println("JSONException Error : "+ e.getMessage());
//                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("VolleyError Error : "+ error.getMessage());
//                Toast.makeText(getActivity().getApplicationContext(), "Route Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        queue.add(stringRequest);
//
//    }
//
//    class MyAdapter extends ArrayAdapter<String>{
//        Context context;
//        String msg[];
//        int senderId[];
//        int msgId[];
//
//        MyAdapter(Context c, String msg[], int senderId[], int msgId[]){
//            super(c, R.layout.item_message, R.id.textLeft, msg);
//            this.context = c;
//            this.msgId = msgId;
//            this.msg = msg;
//            this.senderId = senderId;
//        }
//
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View row = layoutInflater.inflate(R.layout.item_message,parent,false);
//            RelativeLayout leftMessage = (RelativeLayout) row.findViewById(R.id.leftMessage);
//            CardView RightLayout = (CardView) row.findViewById(R.id.RightLayout);
//
//            if (senderId[position] != 0 && senderId[position] !=0) {
//                if (UserId == senderId[position]) {
//                    leftMessage.setVisibility(View.GONE);
//
//                    TextView message_text = row.findViewById(R.id.textRight);
//
//                    message_text.setText(msg[position]);
//
//                } else {
//                    TextView message_sender = row.findViewById(R.id.textNameleft);
//                    RightLayout.setVisibility(View.GONE);
//                    TextView message_text = row.findViewById(R.id.textLeft);
//                    if (LastId != senderId[position]) {
//                        message_sender.setText(Integer.toString(senderId[position]));
//                    } else {
//                        message_sender.setVisibility(View.GONE);
//
//                    }
//                    message_text.setText(msg[position]);
//                }
//
//                LastId = senderId[position];
//                lastPosition = position;
//            }
//
//
//
//            return row;
//        }
//    }
//
//    public boolean sendMessageToServer(String Message, int userId, int courseid){
//        final boolean[] send = {true};
//
////        RequestQueue queue = Volley.newRequestQueue(getActivity());
////        try {
////            JSONObject jsonBody = new JSONObject();
////            jsonBody.put("courseid", courseid);
////            jsonBody.put("msg", Message);
////            jsonBody.put("userid", userId);
////            final String requestBody = jsonBody.toString();
////
////            StringRequest stringRequest = new StringRequest(Request.Method.POST, WsConfig.Base_URL+"/message/addMessage",
////                    new Response.Listener<String>() {
////                        @Override
////                        public void onResponse(String response) {
////                            if(response.equals("200")){
////                                send[0] = true;
////                            }else{
////                                Toast.makeText(getActivity().getApplicationContext(),"you are not connected",Toast.LENGTH_LONG).show();
////                            }
////                        }
////                    }, new Response.ErrorListener() {
////                @Override
////                public void onErrorResponse(VolleyError error) {
////                    System.out.println(error.getMessage());
////                }
////            }){
////                @Override
////                public String getBodyContentType() {
////                    return "application/json; charset=utf-8";
////                }
////
////                @Override
////                public byte[] getBody() throws AuthFailureError {
////                    try {
////                        return requestBody == null ? null : requestBody.getBytes("utf-8");
////                    } catch (UnsupportedEncodingException uee) {
////                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
////                        return null;
////                    }
////                }
////
////                @Override
////                protected Response<String> parseNetworkResponse(NetworkResponse response) {
////                    String responseString = "";
////                    if (response != null) {
////                        responseString = String.valueOf(response.statusCode);
////                        // can get more details such as response.headers
////                    }
////                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
////                }
////            };
////
////            queue.add(stringRequest);
////        }catch (JSONException e){
////            System.out.println(e.getMessage());
////        }
//
//        return send[0];
//
//    }
//
//
//
//}