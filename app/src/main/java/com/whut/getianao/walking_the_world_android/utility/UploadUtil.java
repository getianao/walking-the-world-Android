package com.whut.getianao.walking_the_world_android.utility;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @description: 上传工具类
 * @author: Administrator
 * @date: 2019/5/29 0029
 */
public class UploadUtil {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码


    /**
     * 上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容 JSON格式
     */
    public static JSONObject uploadFile(File file, String RequestURL) {
        JSONObject result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
                /**
                 * 当文件不为空时执行上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuilder sb = new StringBuilder();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名
                 */
                sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                // 请求头
                dos.write(sb.toString().getBytes());
                // 将文件流读入，发送给服务器
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                // 请求的结束字符
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * * 获取响应码 200=成功 当响应成功，获取响应的流
                 * */
                int res = conn.getResponseCode();
                Log.e(TAG, "Http请求回复状态码:" + res);
                if (res == 200) {
                    Log.e(TAG, "请求成功！");
                    InputStream input = conn.getInputStream();
                    StringBuilder sb1 = new StringBuilder();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    // 解析服务器返回的数据，从string转为JSON
                    result = new JSONObject(sb1.toString());
                    Log.e(TAG, "请求回复结果 : " + result);
                } else {
                    Log.e(TAG, "请求失败！");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void uploadShare(Map<String,Object> data, String requestURL) throws IOException {
        // 1.定义请求url
        URL url = new URL(requestURL);
        // 2.建立一个http的连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);//设置连接超时时间
        conn.setReadTimeout(5000); //设置读取的超时时间
        // 3.设置一些请求的参数
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",  "application/json");
        // 序列化
        JSONObject json_data=new JSONObject(data);
        String s=String.valueOf(json_data);
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
}
