package com.czxy.bos.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 带参数发送get请求
 */
public class HttpGetTest02 {


    public static void main(String[] args) throws  Exception{

        // 创建浏览器HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建HttpGet请求方式，并且传递参数
        HttpGet get = new HttpGet("http://www.baidu.com/s?wd=php");

        // 发送请求 + 获取响应结果
        CloseableHttpResponse response = httpClient.execute(get);
        // 打印结果
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));

        // 关闭请求
        response.close();
        httpClient.close();
    }


}
