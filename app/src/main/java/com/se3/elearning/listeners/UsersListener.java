package com.se3.elearning.listeners;

import com.se3.elearning.model.User;

public interface UsersListener {

    void initiateVideoMeeting(User user);
    void initiateAudioMeeting(User user);
    void onMultipleUsersAction(Boolean isMultipleUsersSelected);
}
