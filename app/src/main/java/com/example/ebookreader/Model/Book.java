/*
 * Copyright (c) 2019.
 * Gigara @ G Soft Solutions
 */

package com.example.ebookreader.Model;

import com.google.firebase.database.PropertyName;

public class Book {
    private String name, Image, Author, CategoryId;
    private String Introduction;
    private int Isbn;
    private String url;

    public Book() {
    }

    public Book(String name, String image, String author, String categoryId, String introduction, int isbn, String url) {
        this.name = name;
        Image = image;
        Author = author;
        CategoryId = categoryId;
        this.Introduction = introduction;
        Isbn = isbn;
        this.url = url;
    }

    @PropertyName("Name")
    public String getName() {
        return name;
    }

    @PropertyName("Name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("Image")
    public String getImage() {
        return Image;
    }

    @PropertyName("Image")
    public void setImage(String image) {
        Image = image;
    }

    @PropertyName("Author")
    public String getAuthor() {
        return Author;
    }

    @PropertyName("Author")
    public void setAuthor(String author) {
        Author = author;
    }

    @PropertyName("CategoryId")
    public String getCategoryId() {
        return CategoryId;
    }

    @PropertyName("CategoryId")
    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    @PropertyName("Isbn")
    public int getIsbn() {
        return Isbn;
    }

    @PropertyName("Isbn")
    public void setIsbn(int isbn) {
        this.Isbn = isbn;
    }

    @PropertyName("Introduction")
    public String getIntroduction() {
        return Introduction;
    }

    @PropertyName("Introduction")
    public void setIntroduction(String introduction) {
        this.Introduction = introduction;
    }

    @PropertyName("Url")
    public String getUrl() {
        return url;
    }

    @PropertyName("Url")
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return this.name + " - " + this.Author;
    }
}
