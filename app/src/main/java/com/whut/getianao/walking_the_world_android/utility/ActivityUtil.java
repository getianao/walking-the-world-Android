package com.whut.getianao.walking_the_world_android.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.whut.getianao.walking_the_world_android.MyApplication;
import com.whut.getianao.walking_the_world_android.data.Dym;
import com.whut.getianao.walking_the_world_android.data.User;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2019/6/3 0003
 */
public class ActivityUtil {
    /**
     * @param
     * @return 0--成功 1--失败
     * @description 新建一条动态信息
     * @author Liu Zhian
     * @time 2019/6/3 0003 上午 8:42
     */
    public static int addActivity(Map<String, Object> data) throws IOException {
        // 1.定义请求url
        URL url = new URL("http://"+MyApplication.SERVER_IP+":8080/activity/publish");
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
        // 200 OK
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
            try {
                JSONObject resultJSON = new JSONObject(result);
                int resultCode = (int) resultJSON.get("status");
                if (resultCode == 0) {
                    System.out.println(resultCode);
                    System.out.println("发送成功！");
                    return resultCode;
                } else {
                    System.out.println("发送失败！");
                    return resultCode;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("解析请求返回的JSON时失败！");
                return 1;
            }
        }
        // 返回码不是200，返回失败码（1）
        return 1;
    }


    public static List<Dym> JSON2Activity(JSONObject josnData)
    {
        List<Dym> dyms=null;
        try {
            JSONArray dymsList = josnData.getJSONArray("res"); //获取JSONArray
            int length = dymsList.length();
            dyms = new ArrayList<>();
            for (int i = 0; i < length; i++) {//遍历JSONArray
                JSONObject obj = dymsList.getJSONObject(i);
                Dym dym = new Dym();
                dym.setCreatedTime(new Date(Long.parseLong(obj.getString("createdTime"))));
                dym.setId(obj.getInt("id"));
                dym.setImgUrls(obj.getString("imgUrls"));
                dym.setLikeCount(obj.getInt("likeCount"));
                dym.setStatus(obj.getInt("status"));
                dym.setText(obj.getString("text"));
                dym.setTitle(obj.getString("title"));
                dym.setUserId(obj.getInt("userId"));
                dyms.add(dym);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dyms;
    }


    /**
     * @param
     * @return 0--成功 1--失败
     * @description 获得指定用户的所有好友动态
     * @author Liu Zhian
     * @time 2019/6/3 0003 上午 8:45
     */
        public static JSONObject getAllActivities(int userId) {
        JSONObject result = null;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format("http://"+MyApplication.SERVER_IP+":8080/activity/friendCircle?userId=%d", userId));
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
     * @param
     * @return 0--成功 1--失败
     * @description 获得指定动态的所有评论信息
     * @author Liu Zhian
     * @time 2019/6/3 0003 上午 8:45
     */
    public static JSONObject getAllComments(int activityID) {
        JSONObject result = null;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format("http://"+MyApplication.SERVER_IP+":8080/comment/get?activityId=%d", activityID));
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
     * @param
     * @return 0--成功 1--失败
     * @description 新建一条评论信息
     * @author Liu Zhian
     * @time 2019/6/3 0003 上午 8:42
     */
    public static int addComment(String text, int activityID, int userID) throws IOException {
        // 1.定义请求url
        URL url = new URL("http://10.120.174.62:8080/comment/add");
        // 2.建立一个http的连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);//设置连接超时时间
        conn.setReadTimeout(5000); //设置读取的超时时间
        // 3.设置一些请求的参数
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        // 序列化
        JSONObject json_data = new JSONObject();
        try {
            json_data.put("text",text);
            json_data.put("activityId",activityID);
            json_data.put("userId",userID);
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;  //  包装JSON给后端时失败，也直接返回1
        }
        String s = String.valueOf(json_data);
        // 4.一定要记得设置 把数据以流的方式写给服务器
        conn.setDoOutput(true); // 设置要向服务器写数据
        conn.getOutputStream().write(s.getBytes());

        int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
        // 200 OK
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
            try {
                JSONObject resultJSON = new JSONObject(result);
                int resultCode = (int) resultJSON.get("status");
                if (resultCode == 0) {
                    System.out.println(resultCode);
                    System.out.println("发送成功！");
                    return resultCode;
                } else {
                    System.out.println("发送失败！");
                    return resultCode;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("解析请求返回的JSON时失败！");
                return 1;
            }
        }
        // 返回码不是200，返回失败码（1）
        return 1;
    }

    /**
     * @param
     * @return 0--成功 1--失败
     * @description 某个用户给某个动态点赞
     * @author Liu Zhian
     * @time 2019/6/3 0003 上午 9:19
     */
    public static int thumbUp(int activityID, int userID) {
        int resultCode = -1;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
           // URL url = new URL(String.format("http://10.120.174.62:8080/like/activity?activityId=%d&userId=%d", activityID, userID));
            URL url = new URL(String.format("http://"+MyApplication.SERVER_IP+":8080/like/activity?activityId=%d&userId=%d", activityID, userID));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
            // System.out.println(sb.toString());
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            JSONObject result = new JSONObject(sb.toString());
            resultCode = (int) result.get("status");
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

    //加载图片
    public static Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

}
