package com.example.taha.dizidark01.Model;

public class Comment {
    String userId;
    String yorum;
    String yorumId;

    public String getYorumId() {
        return yorumId;
    }

    public void setYorumId(String yorumId) {
        this.yorumId = yorumId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getYorum() {
        return yorum;
    }

    public void setYorum(String yorum) {
        this.yorum = yorum;
    }
}
