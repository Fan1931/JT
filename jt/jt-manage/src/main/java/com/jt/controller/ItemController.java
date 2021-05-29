package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.service.ItemService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * url: http://localhost:8091/item/query?page=1&rows=50
	 * 参数：page rows
	 * 返回值结果：EasyUITable vo
	 */
	@RequestMapping("/query")
	public EasyUITable findItemByPage(Integer page,Integer rows){

		return itemService.findItemByPage(page, rows);
	}

	/**
	 * 注意事项：如果采用多个参数接收页面数据，则需要注意不要出现重名属性
	 * name="itemId" 将会赋值给全部名称为itemId的属性
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item, ItemDesc itemDesc){
//		try{
//			itemService.saveItem(item);
//			return SysResult.success();
//		}catch (Exception e){
//			e.printStackTrace();
//			return SysResult.fail();
//		}
		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}

	/**
	 * 页面回显调用流程
	 * 1.当修改按钮点击时，开始执行工具栏中的操作
	 * 2.动态的获取用户选中的数据，之后转化为字符串，中间使用“，”隔开
	 * 3.之后根据ids判断是否没选，或者是否多选
	 * 4.根据id选择器，选中窗口div进行弹出框操作
	 * 5.当弹出框操作完成之后，div发起href请求动态获取编辑页面信息
	 * 6.当窗口弹出之后，开始动态的实现数据的回显
	 * 7.同时发起ajax请求，动态的获取商品详情数据信息
	 */

	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId){
		//1.根据id查询商品详情信息
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		//itemDesc中的itemDesc属性是html中的片段
		return SysResult.success(itemDesc);
	}

	/**
	 * 实现商品信息的修改
	 * url：/item/update
	 * 参数：item数据和itemDesc数据
	 * 返回值：Sysresult对象
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc){

		itemService.updateItem(item, itemDesc);
		return SysResult.success();
	}

	/**
	 * 商品的删除
	 * url:item/delete
	 * 参数：ids
	 * 返回值：SysResult
	 */
	@RequestMapping("/delete")
	 public SysResult deleteItem(Long... ids){
		itemService.deleteItems(ids);
		return SysResult.success();
	 }

	/**
	 * 商品的上架、下架
	 * 业务需求：上架status=1 下架status=2
	 * url：item/reshelf上架  item/instock下架
	 * 参数：ids
	 * 返回值：SysResult对象
	 */
	@RequestMapping("/reshelf")
	public SysResult reshelf(Long... ids){
		int status = 1;
		itemService.updateStatus(ids, status);
		return SysResult.success();
	}
	@RequestMapping("/instock")
	public SysResult instock(Long... ids){
		int status = 2;
		itemService.updateStatus(ids, status);
		return SysResult.success();
	}
}
