package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgController {

    @Value("${server.port}")
    private Integer port;

    /**
     * 编辑方法，动态返回当前服务器端口号信息
     */
    @RequestMapping("/getMsg")
    public String getMsg(){
        return "当前你那访问的服务器是：" + port;
    }
}
