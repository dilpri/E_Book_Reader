package com.example.ebookreader.Model;

public class Book {
    private String Name, Image, Author, CategoryId;

    public Book() {

    }

    public Book(String name, String image, String author, String categoryId) {
        Name = name;
        Image = image;
        Author = author;
        CategoryId = categoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }
}
