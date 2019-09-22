/*
 * Copyright (c) 2019.
 * Gigara @ G Soft Solutions
 */

package com.example.ebookreader.Model;

public class Comment {
    private String comment;
    private String date;
    private String user;
    private String uid;

    public Comment() {
    }

    public Comment(String comment, String date, String user, String uid) {
        this.comment = comment;
        this.date = date;
        this.user = user;
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return comment + " - " + user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment1 = (Comment) o;
        return comment.equals(comment1.comment) &&
                date.equals(comment1.date) &&
                user.equals(comment1.user) &&
                uid.equals(comment1.uid);
    }
}
