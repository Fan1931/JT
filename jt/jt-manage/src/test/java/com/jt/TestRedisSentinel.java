package com.jt;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class TestRedisSentinel {

    /**
     * 如果需要实现redis高可用，则只需要用户链接代理哨兵
     * masterName:主机变量名
     * sentinels:哨兵集合信息
     */
    @Test
    public void testSentinel(){
        Set<String> sentinels = new HashSet<>();
        //链接服务host:port
        sentinels.add("192.168.126.129:26379");
        JedisSentinelPool pool = new JedisSentinelPool("mymaster",sentinels);
        Jedis jedis = pool.getResource();//利用哨兵机制，只能操作主机
        jedis.set("hello", "测试哨兵");
        System.out.println(jedis.get("hello"));
    }
}
