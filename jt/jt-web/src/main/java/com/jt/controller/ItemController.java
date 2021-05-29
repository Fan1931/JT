package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;
    /**
     * 请求：http://www.jt.com/items/562379.html
     * 请求分析：通过jt-web服务器 访问后台商品的信息，其中562379表示商品ID
     *         根据商品id号，之后查询后台数据库，获取商品信息之后进行数据的页面展现
     * 参数接受:需要动态接受url中的参数，利用restFul风格
     * 页面展现数据要求：
     * 			<h1>${item.title }</h1>
     * 			<strong>${item.sellPoint}</strong>
     * 		  需要查询后台数据之后，将数据保存到域，之后利用el表达式完成数据的动态取值
     */

    @RequestMapping("/{itemId}")
    public String findItemById(@PathVariable Long itemId, Model model){
        //根据商品id查询数据库
        Item item = itemService.findItemById(itemId);
        //将item对象保存到域
        model.addAttribute("item", item);
        //查询商品详情信息
        ItemDesc itemDesc = itemService.findItemDescById(itemId);
        model.addAttribute("itemDesc", itemDesc);
        return "item";
    }
}
