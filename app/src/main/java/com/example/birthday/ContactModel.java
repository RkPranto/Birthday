package com.example.birthday;

public class ContactModel {
    private int id;
    private  String name, contact;
    private int day, month, year;

    public ContactModel(int id, String name, String contact, int day, int month, int year) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public ContactModel(String name, String contact, int day, int month, int year) {
        this.name = name;
        this.contact = contact;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
