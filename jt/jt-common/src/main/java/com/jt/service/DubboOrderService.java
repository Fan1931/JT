package com.jt.service;

import com.jt.pojo.Order;

public interface DubboOrderService {

    String saveOrder(Order order);

    Order findOrderById(String orderId);
}
