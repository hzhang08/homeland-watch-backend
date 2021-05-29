package com.homelandwatch.model;

public class UserDAO {
    int userID;
    String name;
    String photoUrl;
    int age;
    boolean gender;
    int credit;
    String address;
    boolean isVolunteer;


    public UserDAO(int userID, String name, String photoUrl, int age, boolean gender, int credit, String address, boolean isVolunteer) {
        this.userID = userID;
        this.name = name;
        this.photoUrl = photoUrl;
        this.age = age;
        this.gender = gender;
        this.credit = credit;
        this.address = address;
        this.isVolunteer = isVolunteer;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isVolunteer() {
        return isVolunteer;
    }

    public void setVolunteer(boolean volunteer) {
        isVolunteer = volunteer;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
