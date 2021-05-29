package com.jt.web.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//返回JSON串，需要配置RestController，该controller只接收前台web发来的请求
@RestController
@RequestMapping("/web/item")
public class WebItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 访问url规则：
     *  1.请求协议：http/https/udp/tcp
     *  2.请求主机的地址 域名/IP地址
     *  3.请求的端口 默认80
     *  4.业务名称
     *  5.方法名称
     *  6.如果有参数可以拼接参数
     *
     * 从前台发起请求：
     *  http://manage.jt.com:80/web/item/findItemById/562379
     *
     * 对象转化为JSON，需要调用对象的get方法，动态转化JSON
     * JSON转化为对象，需要调用方法的set
     */

    @RequestMapping("/findItemById/{itemId}")
    public Item findItemById(@PathVariable Long itemId){
        return itemService.findItemById(itemId);
    }

    /**
     * 根据商品id号，动态查询商品详情信息
     */
    @RequestMapping("/findItemDescById/{itemId}")
    public ItemDesc findItemDescById(@PathVariable Long itemId){
        return itemService.findItemDescById(itemId);
    }
}
