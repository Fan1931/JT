package com.jt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

    /**
     * Spring整合Redis
     *  1.spring整合单台redis
     *  2.为了实现Redis内存的扩容，引入redis分片概念
     *  3.为了实现Redis高可用的效果
     */

    /**
     * 配置redis集群
     */
    @Value("${redis.cluster}")
    private String nodes;

    @Bean
    public JedisCluster jedisCluster(){
        Set<HostAndPort> set = new HashSet<>();
        //将节点拆分
        String[] nodeArray = nodes.split(",");
        for (String node : nodeArray) {
            String host = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            set.add(new HostAndPort(host, port));
        }
        return new JedisCluster(set);
    }

    /**
     * 核心思想：
     * 1.动态创建redis哨兵的池对象
     * 2.动态的从池中获取jedis对象，实现数据的存取
     */

//    @Value("${redis.sentinel}")
//    private String sentinel;
//
//    @Bean
//    public JedisSentinelPool jedisSentinelPool(){
//        Set<String> sentinels = new HashSet<>();
//        sentinels.add(sentinel);
//        return new JedisSentinelPool("mymaster", sentinels);
//    }
//
//    //如果Bean注解中添加了参数，该参数spring容器中已经管理，则可以实现动态赋值
//    @Bean
//    public Jedis jedis(JedisSentinelPool jedisSentinelPool){
//        return jedisSentinelPool.getResource();
//    }


//    @Value("${redis.shards}")
//    private String nodes;  //node,node,node
//
//    //spring整合redis分片机制
//    @Bean
//    public ShardedJedis shardedJedis(){
//        List<JedisShardInfo> shards = new ArrayList<>();
//        String[] nodeArray = nodes.split(",");
//        for (String node : nodeArray) {
//            String host = node.split(":")[0];
//            int port = Integer.parseInt(node.split(":")[1]);
//            JedisShardInfo info = new JedisShardInfo(host, port);
//            shards.add(info);
//        }
//        return new ShardedJedis(shards);
//    }

//    @Value("${redis.host}")
//    private String node;
//
//    //手动封装redis对象
//    @Bean //(Value="默认的条件下，都是方法的名字")
//    public Jedis jedis(){
//        String[] nodeArray = node.split(":");
//        String host = nodeArray[0];
//        int port = Integer.parseInt(nodeArray[1]);
//        return new Jedis(host, port);
//    }

    /**
     * 知识的总结：
     *   Spring中如何管理对象，采用Map集合管理spring容器中的对象
     *   Map<bean的ID，实例化的对象>
     *
     *   beanID的语法：
     *     1.xml映射文件中的bean标签的ID属性
     *     2.bean注解的方法名称
     */
}
