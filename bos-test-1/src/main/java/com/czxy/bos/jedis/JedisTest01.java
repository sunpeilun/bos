package com.czxy.bos.jedis;

import redis.clients.jedis.Jedis;

public class JedisTest01 {

    public static void main(String[] args) {
        /**
         * 在本地，redis默认的地址：loclahost     127.0.0.1       ip地址
         * 端口：6379
         */
        // 默认连接localhost:6379
        Jedis jedis = new Jedis();
        //jedis.set("info","好样的，你还在睡觉！！");
        // 赋值的时候设置值的存活时间
        jedis.setex("info",10,"java1班");

        // 取值
        String info = jedis.get("info");
        System.out.println(info);

        //jedis.del("info");

        // 取值
//        info = jedis.get("info");
//        System.out.println(info);
        // 关闭连接
        jedis.close();

    }


}
