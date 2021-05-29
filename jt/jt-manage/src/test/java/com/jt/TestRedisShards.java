package com.jt;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.List;

public class TestRedisShards {

    /**
     * 目的：实现redis内存扩容，采用分片机制
     * 程序需要动态链接3台redis，当用户需要进行存取数据的时候，需要挑选一台redis进行访问
     */
    @Test
    public void testShards(){
        List<JedisShardInfo> shards = new ArrayList<>();
        shards.add(new JedisShardInfo("192.168.126.129", 6379));
        shards.add(new JedisShardInfo("192.168.126.129", 6380));
        shards.add(new JedisShardInfo("192.168.126.129", 6381));
        ShardedJedis shardedJedis = new ShardedJedis(shards);
        shardedJedis.set("shards", "我是redis分片机制");
        String value = shardedJedis.get("shards");
        System.out.println(value);
    }
}
