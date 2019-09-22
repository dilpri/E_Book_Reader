/*
 * Copyright (c) 2019.
 * Gigara @ G Soft Solutions
 */

package com.example.ebookreader.Model;

public class Order {
    private int book;
    private String user;
    private String time;

    public Order() {
    }

    public Order(int book, String user, String time) {
        this.book = book;
        this.user = user;
        this.time = time;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
