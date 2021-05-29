package com.jt.interceptor;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//springMVC对外提供的拦截器接口
@Component //将该对象交给spring容器管理
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 说明：如果用户没有登录，应该重定向到用户的登录页面，如果用户以及登录，则拦截器放行
     * 如何确定用户已经登录？
     *   1.确定用户的cookie中是否有值
     *   2.判断用户的ticket信息，在redis中是否有值
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;
        //1.判断cookie是否有记录
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("JT_TICKET".equals(cookie.getName())) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        //2.校验"JT_TICKET"是否有效,查询redis
        if (!StringUtils.isEmpty(ticket)) {
            if (jedisCluster.exists(ticket)) {
                //校验当前ticket信息是否正确  证明用户已经登陆
                String userJSON = jedisCluster.get(ticket);
                User user = ObjectMapperUtil.toObject(userJSON, User.class);
                //利用Thread实现
                //风险大，建议在同一个累赘使用
                //UserThreadLocal.set(user);

                //可以利用request域对象，保存数据
                request.getSession().setAttribute("JT_USER",user);
                return true;
            }
        }
        response.sendRedirect("/user/login.html");
        return false; //表示拦截
    }
}
