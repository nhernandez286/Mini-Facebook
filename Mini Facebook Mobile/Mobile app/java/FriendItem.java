package com.example.facebook;
import android.graphics.drawable.Drawable;
public class FriendItem {
    public String name;
    public String degree;
    public String school;
    public String location;
    public Drawable profileImage;
    public String imageURL;

    public String userName;

    public boolean isFriend;
    public String gender;
    public String age;

    public FriendItem(String name, String degree, String school, String location, String imageURL) {
        this.name = name;
        this.degree = degree;
        this.school = school;
        this.location = location;
        this.imageURL = imageURL;
        this.profileImage = null;
    }
    public FriendItem(String name, String degree, String school, String location, Drawable profileImage, String userName, boolean isFriend, String imageURL, String gender, String age) {
        this.name = name;
        this.degree = degree;
        this.school = school;
        this.location = location;
        this.profileImage = profileImage;
        this.userName = userName;
        this.isFriend = isFriend;
        this.imageURL = imageURL;
        this.gender = gender;
        this.age = age;
    }

}
