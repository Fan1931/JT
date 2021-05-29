package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference(check = false)
    private DubboCartService cartService;

    @Reference(check = false)
    private DubboOrderService orderService;

    /**
     * 跳转到订单确认页
     * url:http://www.jt.com/order/create.html
     * 页面的取值:${carts}
     */
    @RequestMapping("/create")
    public String orderCreate(Model model, HttpServletRequest request){//利用request域
        User user = (User) request.getSession().getAttribute("JT_USER");
        Long userId = user.getId();
        List<Cart> cartList = cartService.findCartListByUserId(userId);
        model.addAttribute("carts", cartList);
        return "order-cart"; //跳转到订单确认页面
    }

    /**
     * 1.实现订单入库
     * url:http://www.jt.com/order/submit
     * 参数:post提交form表单 对象接受
     * 返回值: SysResult 要求返回orderId
     */
    @RequestMapping("/submit")
    @ResponseBody
    public SysResult saveOrder(Order order,HttpServletRequest request){
        //1.动态获取userId
        User user = (User) request.getSession().getAttribute("JT_USER");
        Long userId = user.getId();
        order.setUserId(userId);
        //2.订单入库
        String orderId = orderService.saveOrder(order); //包含了三个对象数据
        if (!StringUtils.isEmpty(orderId)) {
            return SysResult.success(orderId);
        }
        return SysResult.fail();
    }

    /**
     * 业务：根据orderId查询订单信息
     * url:http://www.jt.com/order/success.html?id=6546168
     * 参数:id
     * 返回值:String 类型具体页面
     */
    @RequestMapping("/success")
    public String success(@RequestParam("id") String orderId,Model model) {
        //1.根据orderId查询order对象信息
        Order order = orderService.findOrderById(orderId);
        model.addAttribute("order", order);
        return "success";
    }
}
