package com.czxy.bos.httpclient;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpGetTest01 {
    /**
     * 模拟浏览器发送get请求
     *
     *
     * @param args
     */
    public static void main(String[] args) throws  Exception{

        //1 打开浏览器对象----->创建HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //2 输入地址      ----->创建url对象--创建url对象的时候，需要同时创建请求对象
        //有哪些请求对象呢？HttpGet、HttpPost、HttpPut、HttpDelete
        HttpGet get = new HttpGet("http://www.baidu.com");

        //3 按下enter键   ----->发送请求 httpClient.execute
        CloseableHttpResponse response=  httpClient.execute(get);

        //4 获取响应结果:状态码+响应体
        //4.1 状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("状态码"+statusCode);
        //4.2 响应体
        HttpEntity entity = response.getEntity();
        //EntityUtils.toString ： 将HttpEntity转成字符串
        System.out.println(EntityUtils.toString(entity));

        //5 关闭浏览器对象
        response.close();
        httpClient.close();

    }


}
