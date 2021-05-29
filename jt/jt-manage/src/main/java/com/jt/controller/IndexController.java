package com.jt.controller;

import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

	/* 实现通用的页面跳转:restFul风格
	 * 1.参数需要/分割
	 * 2.参数需要{}包裹
	 * 3.需要添加方法的参数，并且利用注解实现数据的转化
	 * 简化说明：利用公共的api的方法，实现了后台代码的简化
	 */
//	@RequestMapping("/page/{abd}") //如果名称与参数不一致可以使用value/name实现数据转化
//	public String module(@PathVariable(value = "abc") String moduleName) {
//		return moduleName;
//	}

	@RequestMapping("/page/{moduleName}")
	public String module(@PathVariable String moduleName) {
		return moduleName;
	}

	// 利用RestFul风格简化url，实现crud操作,简化了请求的方式
	//@RequestMapping(value = "/user",method = RequestMethod.GET)
	@GetMapping("/user")
	public User getUserById(int id){
		//return user;
		return null;
	}
	//@RequestMapping(value = "/user",method = RequestMethod.DELETE)
	@DeleteMapping("/user")
	public User deleteUserById(int id){
		//return user;
		return null;
	}

//	//1.实现商品新增跳转
//	@RequestMapping("/page/item-add")
//	public String item_add(){
//		return "item-add";
//	}
//
//	//2.实现商品list页面的跳转
//	@RequestMapping("/page/item-list")
//	public String item_list(){
//		return "item-list";
//	}
}
