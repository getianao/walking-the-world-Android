package com.whut.getianao.walking_the_world_android.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Administrator
 * @date: 2019/6/3 0003
 */
public class SearchLocation {
    private String title;
    private String address;

    public SearchLocation(String title, String address) {
        this.title = title;
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static List<SearchLocation> getFakeDatas()
    {
        List<SearchLocation> res =new ArrayList<>();
        SearchLocation s1=new SearchLocation("武汉理工大学东院","马房山校区");
        SearchLocation s2=new SearchLocation("武汉理工大学西院","马房山校区");
        SearchLocation s3=new SearchLocation("武汉理工大学南湖","南湖校区");
        res.add(s1);
        res.add(s2);
        res.add(s3);
        return res;
    }
}
