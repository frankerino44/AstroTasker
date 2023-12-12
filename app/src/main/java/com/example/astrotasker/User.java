package com.example.astrotasker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private int xp;
    private int profilePhoto;
    private int level;

    private ArrayList<String> friends;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
    public int getProfilePhoto() {
        return profilePhoto;
    }
    public void setProfilePhoto(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    /*public ArrayList<String> getFriends() {
        return friends;
    }
    public void setFriends(Map<String, String> friends) {
        this.friends = new ArrayList<>(friends.values());;
    }*/
}
