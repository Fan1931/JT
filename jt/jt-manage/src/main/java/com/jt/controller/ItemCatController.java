package com.jt.controller;

import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    /**
     *  url:/item/cat/queryItemName
     *  参数：{itemCatId:val}
     *  返回值结果：商品分类的名称
     */

    @RequestMapping("/queryItemName")
    public String queryItemName(Long itemCatId){

        return itemCatService.findItemCatName(itemCatId);
    }

    /**
     * 实现商品分类列表页面的展现
     * url:/item/cat/list
     * 参数：parentId
     * 返回值结果：List<EasyUITree>
     * 异步树空间：当点击父级商品节点时，会发起新的url，并且参数id为xxx，根据父级查询子集
     * 需求：如果必须接受该参数，但是不想修改参数名，则使用如下注解
     */
    @RequestMapping("/list")
    public List<EasyUITree> findEasyUITreeByParentId(@RequestParam(value = "id",defaultValue = "0") Long parentId){
        //Long parentId = 0L;

        return itemCatService.findEasyUITreeByParentId(parentId);
        //return itemCatService.findItemCatListByCache(parentId);
    }
}
