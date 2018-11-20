package com.czxy.bos.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolTest02 {

    public static void main(String[] args) throws Exception{

        // 配置文件
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置池连接数,最大连接数
        config.setMaxIdle(50);
        // 最小连接数
        config.setMinIdle(10);

        // 创建池对象
        // 第一个参数：配置信息
        // 第二个参数：redis的主机地址
        // 第三个参数：redis在主机上的端口号
        JedisPool pool = new JedisPool(config, "127.0.0.1", 6379);
        // 获取连接
        Jedis jedis = pool.getResource();

        jedis.set("info","哈哈");
        String info = jedis.get("info");
        System.out.println(info);
        // 关闭连接
        jedis.close();
        pool.close();

    }



}
