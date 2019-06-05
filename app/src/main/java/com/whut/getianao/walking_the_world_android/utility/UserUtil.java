package com.whut.getianao.walking_the_world_android.utility;

import com.whut.getianao.walking_the_world_android.MyApplication;
import com.whut.getianao.walking_the_world_android.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2019/6/3 0003
 */
public class UserUtil {
    /**
     * @param
     * @return
     * @description 增加一个好友
     * @author Liu Zhian
     * @time 2019/6/3 0003 下午 12:00
     */
     static public String uuuurl="http://"+MyApplication.SERVER_IP+":8080";
    // "http://10.120.174.62:8080"
    public static int addFriend(int userId, int friendId) {
        int result = -1;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format(uuuurl+"/friendship/add?userId=%d&friendId=%d", userId, friendId));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
            // System.out.println(sb.toString());
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            JSONObject resuleJson = new JSONObject(sb.toString());
            result = (int) resuleJson.get("status");
            return result;
        } catch (MalformedURLException me) {
            System.err.println("你输入的URL格式有问题！");
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param
     * @return
     * @description 删除一个好友
     * @author Liu Zhian
     * @time 2019/6/3 0003 下午 12:00
     */
    public static int deleteFriend(int userId, int friendId) {
        int result = -1;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format(uuuurl+"/friendship/delete?userId=%d&friendId=%d", userId, friendId));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
            // System.out.println(sb.toString());
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            JSONObject resuleJson = new JSONObject(sb.toString());
            result = (int) resuleJson.get("status");
            return result;
        } catch (MalformedURLException me) {
            System.err.println("你输入的URL格式有问题！");
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param
     * @return
     * @description 得到所有的好友
     * @author Liu Zhian
     * @time 2019/6/3 0003 下午 12:00
     */
    public static JSONObject allFriends(int userId) {
        JSONObject result = null;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format(uuuurl+"/friendship/getAllFriends?userId=%d", userId)); // 这里要改
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
            // System.out.println(sb.toString());
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            result = new JSONObject(sb.toString());
//            List<User> list=(List<User>)result.get("res");
//            System.out.println();
        } catch (MalformedURLException me) {
            System.err.println("你输入的URL格式有问题！");
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static List<User> JSON2User(JSONObject josnData)
    {
        List<User> users=null;
        try {
            JSONArray friendsList = josnData.getJSONArray("res"); //获取JSONArray
            int length = friendsList.length();
            users = new ArrayList<>();
            for (int i = 0; i < length; i++) {//遍历JSONArray
                JSONObject obj = friendsList.getJSONObject(i);
                User user = new User();
                user.setAge(obj.getInt("age"));
                user.setBornPlace(obj.getString("bornPlace"));
                user.setCompany(obj.getString("company"));
                user.setEmail(obj.getString("email"));
                user.setHeadUrl(obj.getString("headUrl"));
                user.setId(Integer.valueOf(obj.getString("id")));
                user.setName(obj.getString("name"));
                user.setPassword(obj.getString("password"));
                users.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }


    /**
     * @param
     * @return
     * @description 查通过email找好友
     * @author Liu Zhian
     * @time 2019/6/3 0003 下午 12:00
     */
    public static JSONObject queryUser(String email) {
        JSONObject result = null;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format(uuuurl+"/friendship/find?email=%s", email));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
            // System.out.println(sb.toString());
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            result = new JSONObject(sb.toString());
        } catch (MalformedURLException me) {
            System.err.println("你输入的URL格式有问题！");
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    public static JSONObject getFriendInfo(int userId) {
        Map<String, Object> map = null;
        int result = -1;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format(uuuurl+"/user/getInfo?userId=%d", userId));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
            // System.out.println(sb.toString());
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            JSONObject resuleJson = new JSONObject(sb.toString());

          return resuleJson;
        } catch (MalformedURLException me) {
            System.err.println("你输入的URL格式有问题！");
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static User trans(JSONObject josnData){
        User user=null;
        System.out.println(josnData);
        try {
            JSONObject obj = josnData.getJSONObject("res"); //获取JSONArray
            user = new User();
            user.setAge(obj.getInt("age"));
            user.setBornPlace(obj.getString("bornPlace"));//
            user.setCompany(obj.getString("company"));//
            user.setEmail(obj.getString("email"));
            user.setHeadUrl(obj.getString("headUrl"));
            user.setId(Integer.valueOf(obj.getString("id")));
            user.setName(obj.getString("name"));
            user.setPassword(obj.getString("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;

    }

}
