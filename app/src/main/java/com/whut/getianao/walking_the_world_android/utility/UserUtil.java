package com.whut.getianao.walking_the_world_android.utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author: Administrator
 * @date: 2019/6/3 0003
 */
public class UserUtil {
    /**
     * @description 增加一个好友
     * @param
     * @return
     * @author Liu Zhian
     * @time 2019/6/3 0003 下午 12:00
     */
    public static int addFriend(int userId,int friendId)
    {
        int result=-1;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format("http://10.120.174.62:8080/friendship/add?userId=%d&friendId=%d", userId,friendId));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
            // System.out.println(sb.toString());
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            JSONObject resuleJson = new JSONObject(sb.toString());
            result=(int)resuleJson.get("status");
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
     * @description 删除一个好友
     * @param
     * @return
     * @author Liu Zhian
     * @time 2019/6/3 0003 下午 12:00
     */
    public static int deleteFriend(int userId,int friendId)
    {
        int result=-1;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format("http://10.120.174.62:8080/friendship/delete?userId=%d&friendId=%d", userId,friendId));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
            // System.out.println(sb.toString());
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            JSONObject resuleJson = new JSONObject(sb.toString());
            result=(int)resuleJson.get("status");
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
     * @description 得到所有的好友
     * @param
     * @return
     * @author Liu Zhian
     * @time 2019/6/3 0003 下午 12:00
     */
    public static JSONObject allFriends(int userId)
    {
        JSONObject result = null;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format("http://10.120.174.62:8080/friendship/getAllFriends?userId=%d", userId)); // 这里要改
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
     * @description 查通过email找好友
     * @param
     * @return
     * @author Liu Zhian
     * @time 2019/6/3 0003 下午 12:00
     */
    public static JSONObject queryUser(String email)
    {
        JSONObject result = null;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format("http://10.120.174.62:8080/friendship/find?email=%s",email));
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
}
