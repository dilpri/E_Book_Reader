
/*
 * Copyright (c) 2019.
 * Gigara @ G Soft Solutions
 */

package com.example.ebookreader.Model;

public class Contact {
    private String firstLine;
    private String secondLine;
    private String province;
    private int postalCode;
    private int number;

    public Contact() {
    }

    public Contact(String firstLine, String secondLine, String province, int postalCode, int number) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.province = province;
        this.postalCode = postalCode;
        this.number = number;
    }

    public String getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(String firstLine) {
        this.firstLine = firstLine;
    }

    public String getSecondLine() {
        return secondLine;
    }

    public void setSecondLine(String secondLine) {
        this.secondLine = secondLine;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
