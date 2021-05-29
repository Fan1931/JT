package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JedisCluster jedisCluster;

    @RequestMapping("/getMsg")
    public String getMsg(){
        return "单点登录系统配置成功";
    }

    /**
     * 根据web中传递的参数，实现数据校验
     * url:http://sso.jt.com/user/check/{param}/{type}
     * 参数：param type
     * 返回值：SysResult对象
     * 跨域访问，需要数据的封装
     */
    @RequestMapping("/check/{param}/{type}")
    public JSONPObject checkUser(@PathVariable String param,@PathVariable Integer type,String callback){
        //查询后台数据库，检查数据是否存在
        Boolean flag = userService.checkUser(param, type);
        //封装返回值结果
        SysResult sysResult = SysResult.success(flag);
        return new JSONPObject(callback, sysResult);
    }

    /**
     * 业务需求：根据ticket信息，查询用户信息
     * url：http://sso.jt.com/user/query/d98b65dd-00ad-4c2c-aeda-9564f9771183?callback=jsonp1621658109159&_=1621658109213
     * 参数：ticket信息
     * 返回值：SysResult对象
     */
    @RequestMapping("/query/{ticket}")
    public JSONPObject findUserByTicket(@PathVariable String ticket,String callback){
        String userJSON = jedisCluster.get(ticket);
        //判断查询是否正确
        if (StringUtils.isEmpty(userJSON)) {
            //如果为空，则表示数据，没有查询
            SysResult sysResult = SysResult.fail();
            return new JSONPObject(ticket,callback);
        }
        //说明：用户信息没错
        SysResult sysResult = SysResult.success(userJSON);
        return new JSONPObject(callback, sysResult);
    }

}
