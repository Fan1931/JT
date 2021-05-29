package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//需要跳转页面
@Controller
@RequestMapping("/user")
public class UserController {

    //引入dubbo的配置
    @Reference(check = false) //消费者启动时，暂时不检查服务是否有提供者
    private DubboUserService userService;

    @Autowired
    private JedisCluster jedisCluster;

    //实现页面跳转 http://www.jt.com/user/register.html
    //实现页面通用跳转
    @RequestMapping("/{moduleName}")
    public String register(@PathVariable String moduleName){
        return moduleName;
    }

    /**
     * 实现用户注册
     */
    @RequestMapping("/doRegister")
    @ResponseBody
    public SysResult doRegister(User user){
        userService.saveUser(user);
        return SysResult.success();
    }

    /**
     * 业务需求：实现用户登录操作
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public SysResult doLogin(User user, HttpServletResponse response){
        //1.将数据发往jt-sso系统校验
        String uuid = userService.findUserByUP(user);
        //2.校验uuid是否正确
        if (StringUtils.isEmpty(uuid)) {
            //用户名和密码不正确
            return SysResult.fail();
        }
        //3.将用户信息写入cookie
        Cookie cookie = new Cookie("JT_TICKET", uuid);
        cookie.setMaxAge(7*24*3600); //设定cookie保存时间
        cookie.setDomain("jt.com");  //在指定域名中设定cookie数据共享
        cookie.setPath("/");         //cookie作用的范围
        response.addCookie(cookie);

        return SysResult.success();
    }

    /**
     * 业务需求：实现用户的登出操作
     * url：http://www.jt.com/user/logout.html
     * 参数：没有参数
     * 返回值：重定向到系统首页
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        //1.首先获取cookie中的值
        Cookie[] cookies = request.getCookies();
        //2.从数组中动态获取JT_TICKET数据
        String ticket = null;
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie:cookies) {
                if (cookie.getName().equals("JT_TICKET")) {
                    //3.动态获取cookie值
                    ticket = cookie.getValue();
                    //4.删除cookie
                    cookie.setMaxAge(0);
                    cookie.setDomain("jt.com");
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        //5.判断redis中是否存在该数据，如果存在直接删除
        if (jedisCluster.exists(ticket)) {
            jedisCluster.del(ticket);
        }
        //返回重定向
        return "redirect:/";
    }

}
