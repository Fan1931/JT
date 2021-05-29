package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import java.util.Date;
import java.util.UUID;

//这是dubbo的实现类
@Service
public class DubboUserServiceImpl implements DubboUserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JedisCluster jedisCluster; //注入redis集群

    /**
     * 实现用户注册：用户注册时间
     * @param user
     */
    @Override
    public void saveUser(User user) {
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass)
                .setEmail(user.getPhone())
                .setCreated(new Date())
                .setUpdated(user.getCreated());
        userMapper.insert(user);
    }

    /**
     * 核心业务逻辑：根据用户名和密码查询数据库
     * 如果数据库中有记录，则保存redis，之后返回uuid
     * 如果数据库中没有记录，则返回null
     */
    @Override
    public String findUserByUP(User user) {
        //需要将密码加密
        String mds5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(mds5Pass);
        //根据对象中不为空的属性，充当where条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        //查询用户所有信息
        User userDB = userMapper.selectOne(queryWrapper);
        if (userDB == null) {
            //如果为空，表示数据库没有该记录，用户名或密码错误
            return null;
        }
        String key = UUID.randomUUID().toString();
        //为了保护用户隐私信息
        userDB.setPassword("你猜猜！！！");
        String userJSON = ObjectMapperUtil.toJSON(userDB);
        jedisCluster.setex(key, 7 * 24 * 60 * 60, userJSON);
        return key;
    }
}
