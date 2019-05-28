package com.whut.getianao.walking_the_world_android.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
    private String gender;  // 0-男性  1-女性
    private String bio; // 自我简介

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
