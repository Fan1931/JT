package com.jt.web.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JSONPController {

    /**
     * 跨域访问，获取商品详情信息
     * url：http://manage.jt.com/web/testJSONP?callback=hello&_=1621417563009
     * 参数：？callback=jQuery 传递了回调函数名称
     * 返回值：必须特殊的格式封装
     */
    //@RequestMapping("/web/testJSONP")
    public String testJSONP(String callback){
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(1000L).setItemDesc("JSON跨域测试");
        //将对象转化为JSON
        String json = ObjectMapperUtil.toJSON(itemDesc);
        //将数据封装
        return callback + "(" + json + ")";
    }

    /**
     * JSONP的工具API
     * function:
     */
    @RequestMapping("/web/testJSONP")
    public JSONPObject testJSONP2(String callback){
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(1000L).setItemDesc("工具API");
        return new JSONPObject(callback, itemDesc);
    }

}
