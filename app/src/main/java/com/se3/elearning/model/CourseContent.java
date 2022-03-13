package com.se3.elearning.model;

public class CourseContent {
    String classId, name, ranName, time, url;

    public CourseContent(String classId, String name, String ranName, String time, String url) {
        this.classId = classId;
        this.name = name;
        this.ranName = ranName;
        this.time = time;
        this.url = url;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRanName() {
        return ranName;
    }

    public void setRanName(String ranName) {
        this.ranName = ranName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
