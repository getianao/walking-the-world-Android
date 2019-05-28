package com.whut.getianao.walking_the_world_android.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
    private Integer gender;  // 0-男性  1-女性
    private String bio; // 自我简介

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
