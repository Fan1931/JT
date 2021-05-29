package com.jt.service;

import com.jt.pojo.Cart;

import java.util.List;

public interface DubboCartService {

    List<Cart> findCartListByUserId(Long userId);

    void updateNum(Cart cart);

    void deleteCart(Cart cart);

    void saveCart(Cart cart);
}
