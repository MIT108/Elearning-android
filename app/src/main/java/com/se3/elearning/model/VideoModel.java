package com.se3.elearning.model;

public class VideoModel {
    String classId, id, timestamp, description, videoUrl, userName;

    public VideoModel(String classId, String id, String timestamp, String description, String videoUrl, String userName) {
        this.classId = classId;
        this.id = id;
        this.timestamp = timestamp;
        this.description = description;
        this.videoUrl = videoUrl;
        this.userName = userName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
