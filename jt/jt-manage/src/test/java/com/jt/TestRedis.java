package com.jt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;

import java.util.Map;
import java.util.Set;
@SpringBootTest
public class TestRedis {

    @Autowired
    private Jedis jedis;
    /**
     * redis 链接的入门案例
     * 通过Java程序远程链接redis
     * 1.要求linux关闭防火墙
     * 2.redis的配置文件在的IP绑定注释
     * 3.redis的配置文件保护模式关闭
     */

    @Test
    public void testStringSet(){
        String host = "192.168.126.129";
        int port = 6379;
        //1.准备jedis对象
        Jedis jedis = new Jedis(host, port);
        jedis.set("redis", "redis缓存实现");
        System.out.println(jedis.get("redis"));
    }

    /**
     * 2.需求：如果当前key存在，则不允许赋值
     */
    //private Jedis jedis;

//    @BeforeEach
//    public void init(){
//        String host = "192.168.126.129";
//        int port = 6379;
//        //1.准备jedis对象
//        jedis = new Jedis(host, port);
//    }

    //默认规则：采用set方法为redis赋值，会覆盖之前的操作
    @Test
    public void testStringNX(){
        jedis.set("redis", "redis缓存实现");
        //jedis.set("redis", "你好redis！！！");
        jedis.setnx("redis", "测试nx");
        System.out.println(jedis.get("redis"));
    }

    //3.需求：为数据添加超时时间
    @Test
    public void testEX() throws InterruptedException {
        jedis.set("a", "aaa");
        jedis.expire("a", 10);
        Thread.sleep(3000);
        Long live = jedis.ttl("a");
        System.out.println("当前数据还能存活"+live+"秒");
        jedis.persist("a");//撤销失效时间

        //能否将操作合成一步完成，保证:操作的原则性
        jedis.setex("aa", 10, "你好springbot");
        Thread.sleep(3000);
        System.out.println("当前数据还能存活"+jedis.ttl("aa"));
        System.out.println(jedis.get("aa"));
    }

    //4.需求：set操作不允许覆盖之前记录，并且添加超时时间
    @Test
    public void testStringSet2(){
        SetParams params = new SetParams();
        //params.nx().ex(30);   //nx：当key不存在，创建并赋值
        params.xx().ex(30);     //xx：当key存在，赋值
        String result = jedis.set("aa", "1232465156", params);
        System.out.println(result);
    }

    /**
     * 利用hash保存对象的数据
     */
    @Test
    public void testHash(){
        jedis.hset("user", "id", "101");
        jedis.hset("user", "name", "测试");
        //获取对象所有的属性和值
        Map<String ,String > map = jedis.hgetAll("user");
        System.out.println(map);
    }

    /**
     * 测试redis中的list集合
     */
    @Test
    public void testList(){
        jedis.lpush("list", "1","2","3","4");
        String value = jedis.rpop("list");
        System.out.println(value);
    }

    @Test
    public void testSet(){
        jedis.sadd("set", "1", "2" , "3");
        Long num = jedis.scard("set"); //获取集合中元素数量
        System.out.println("获取元素数据" + num);
        Set<String> sets = jedis.smembers("set"); //输出具体元素
        System.out.println(sets);
    }

    @Test
    public void testTx(){
        Transaction transaction = jedis.multi(); //开始事务
        try {
            transaction.set("w","ww");
            transaction.set("k","kk");
            transaction.set("c","cc");
            transaction.exec(); //提交事务
        }catch (Exception e){
            e.printStackTrace();
            transaction.discard(); //事务回滚
        }
    }
}
