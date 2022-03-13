package com.se3.elearning.utilities;

import com.google.android.gms.common.api.HasApiKey;

import java.util.HashMap;

public class Constants {

    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_COLLECTION_COURSE_CONTENT = "Course Content";
    public static final String KEY_USERID = "userid";
    public static final String KEY_ADMINID = "adminid";
    public static final String KEY_CLASSID = "classid";
    public static final String KEY_NAME = "name";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_FN = "FN";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ROLE = "role";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FCM_TOKEN = "fcm_token";
    public static final String KEY_COURSEID = "courseid";
    public static final String KEY_COURSE_NAME = "name";
    public static final String KEY_COURSE_DESCRIPTION= "description";

    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";
    public static final String REMOTE_MSG_INVITATION_ACCEPTED = "accepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "rejected";
    public static final String REMOTE_MSG_INVITATION_CANCELLED = "cancelled";

    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRoom";


    public static HashMap<String, String> remoteMsgHeaders = null;

    public static HashMap<String, String> getRemoteMessageHeaders(){
        if (remoteMsgHeaders == null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAbW2LSr4:APA91bGbsISryg-HCWqWgIP4UHHJ285Y3YsFlSqrnKKYry5fGse0fSAWQLljiILww3Bgz_OmQbJ-4oFuS7T4NcgdO1K9jm9IPpAp3GcEI5_QU7ZJoMljl42sR6C5HP_wElZ1816Vgu8a"
            );
            remoteMsgHeaders.put(REMOTE_MSG_CONTENT_TYPE, "application/json");
        }
        return remoteMsgHeaders;
    }

    //chat variables
    public static final String KEY_USER = "user";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_COLLECTION_CHAT = "privateChat";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_AVAILABILITY = "availability";

    //Group chat variables
    public static final String KEY_RECEIVER_GROUP_ID = "groupId";
    public static final String KEY_COLLECTION_GROUP_CHAT = "groupChat";

    //latest conversation
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";

    //
    public static final String KEY_VIDEO_FOLDER = "Videos";


}
