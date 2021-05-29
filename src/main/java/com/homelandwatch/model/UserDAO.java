package com.homelandwatch.model;

public class UserDAO {
    int userID;
    String name;
    String photoUrl;
    int age;
    Gender gender;
    int credit;
    String address;
    Role role;

    public enum Gender {
        Male,
        Female
    }

    public enum Role {
        Volunteer,
        Elderly
    }

    public UserDAO() {

    }

    public UserDAO(int userID, String name, String photoUrl, int age, Gender gender,
                   int credit, String address, Role role) {
        this.userID = userID;
        this.name = name;
        this.photoUrl = photoUrl;
        this.age = age;
        this.gender = gender;
        this.credit = credit;
        this.address = address;
        this.role = role;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return userID + "," + name + "," + photoUrl +
                "," + age + "," + gender + "," + credit +
                "," + address + "," + role;
    }
}
