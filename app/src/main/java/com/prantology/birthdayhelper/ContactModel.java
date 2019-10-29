package com.prantology.birthdayhelper;

public class ContactModel {
    private int id;
    private  String name, contact;
    private int day, month, year, hour , minute;
    private String wish, msgState, notificationState;


    public ContactModel(String name, String contact, int day, int month, int year, int hour, int minute, String wish, String state) {
        this.name = name;
        this.contact = contact;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.wish = wish;
        this.msgState = state;
    }

    public ContactModel(int id,String name, String contact, int day, int month, int year, int hour, int minute, String wish, String state) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.wish = wish;
        this.msgState = state;
    }

    public ContactModel(String name, String contact, int day, int month, int year, int hour, int minute, String wish, String state, String notificationState) {
        this.name = name;
        this.contact = contact;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.wish = wish;
        this.msgState = state;
        this.notificationState = notificationState;
    }

    public ContactModel(int id,String name, String contact, int day, int month, int year, int hour, int minute, String wish, String state, String notificationState) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.wish = wish;
        this.msgState = state;
        this.notificationState = notificationState;
    }


    public ContactModel(int id,String name, String contact, int day, int month, int year, int hour, int minute, String wish) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.wish = wish;
    }



    public ContactModel(String name, String contact, int day, int month, int year, int hour, int minute, String wish) {
        this.name = name;
        this.contact = contact;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.wish = wish;
    }

    public ContactModel(int id, String name, String contact, int day, int month, int year) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public ContactModel(String name, String contact, int day, int month, int year, int hour, int min) {
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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public String getMsgState() {
        return msgState;
    }

    public void setMsgState(String msgState) {
        this.msgState = msgState;
    }

    public String getNotificationState() {
        return notificationState;
    }

    public void setNotificationState(String notificationState) {
        this.notificationState = notificationState;
    }
}
