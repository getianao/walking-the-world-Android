package com.whut.getianao.walking_the_world_android.utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2019/6/3 0003
 */
public class ActivityUtil {
    /**
     * @param
     * @return
     * @description 新建一条动态信息
     * @author Liu Zhian
     * @time 2019/6/3 0003 上午 8:42
     */
    public static void addActivity(Map<String, Object> data) throws IOException {
        // 1.定义请求url
        URL url = new URL("http://10.120.174.62:8080/activity/publish");
        // 2.建立一个http的连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);//设置连接超时时间
        conn.setReadTimeout(5000); //设置读取的超时时间
        // 3.设置一些请求的参数
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        // 序列化
        JSONObject json_data = new JSONObject(data);
        String s = String.valueOf(json_data);
        conn.setRequestProperty("Content-Length", s.length() + "");
        // 4.一定要记得设置 把数据以流的方式写给服务器
        conn.setDoOutput(true); // 设置要向服务器写数据
        conn.getOutputStream().write(s.getBytes());

        int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
        // // 503服务器内部错误
        if (code == 200) {
            InputStream is = conn.getInputStream();
            // 把is的内容转换为字符串
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            String result = new String(bos.toByteArray());
            is.close();
            System.out.println("发送成功！");

        } else {
            System.out.println("发送失败！");
        }
    }

    /**
     * @param
     * @return 0--成功 1--失败
     * @description 获得指定email用户的所有好友动态
     * @author Liu Zhian
     * @time 2019/6/3 0003 上午 8:45
     */
    public static int getAllActivities(String email) {
        int resultCode = -1;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format("http://10.120.174.62:8080/reg.do?email=%s", email));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
            // System.out.println(sb.toString());
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            JSONObject result = new JSONObject(sb.toString());
            resultCode = (int) result.get("code");
            System.out.println(resultCode);
        } catch (MalformedURLException me) {
            System.err.println("你输入的URL格式有问题！");
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultCode;
    }
    /**
     * @param
     * @return
     * @description 新建一条评论信息
     * @author Liu Zhian
     * @time 2019/6/3 0003 上午 8:42
     */
    public static void addComment(String text,String activityID,String userID) throws IOException {
        // 1.定义请求url
        URL url = new URL("http://10.120.174.62:8080/reg.do");  // 这里要改
        // 2.建立一个http的连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);//设置连接超时时间
        conn.setReadTimeout(5000); //设置读取的超时时间
        // 3.设置一些请求的参数
        conn.setRequestMethod("GET");
        // 4. 返回结果
        int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
        // // 503服务器内部错误
        if (code == 200) {
            InputStream is = conn.getInputStream();
            // 把is的内容转换为字符串
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            String result = new String(bos.toByteArray());
            is.close();
            System.out.println(result);
            System.out.println("发送动态成功！");

        } else {
            System.out.println("发送动态失败！");
        }
    }
    /**
     * @description 某个用户给某个动态点赞
     * @param
     * @return
     * @author Liu Zhian
     * @time 2019/6/3 0003 上午 9:19
     */
    public static int thumbUp(String activityID,String userID)
    {
        int resultCode = -1;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format("http://10.120.174.62:8080/reg.do?activityID=%s&userID=%s", activityID,userID));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
            // System.out.println(sb.toString());
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            JSONObject result = new JSONObject(sb.toString());
            resultCode = (int) result.get("code");
            System.out.println(resultCode);
        } catch (MalformedURLException me) {
            System.err.println("你输入的URL格式有问题！");
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultCode;

    }
}
