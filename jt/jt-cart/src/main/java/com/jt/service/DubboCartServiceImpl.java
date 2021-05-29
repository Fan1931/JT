package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

//这是dubbo的实现类
@Service
public class DubboCartServiceImpl implements DubboCartService{

    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<Cart> findCartListByUserId(Long userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return cartMapper.selectList(queryWrapper);
    }

    /**
     * 根据itemId userId 修改购物车记录信息
     * 修改记录:商品数量，修改时间
     */
    @Override
    public void updateNum(Cart cart) {
        Cart cartTemp = new Cart();
        cartTemp.setNum(cart.getNum())
                .setUpdated(new Date());
        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("item_id", cart.getItemId())
                     .eq("user_id", cart.getUserId());
        //根据其中不为null的元素当中set条件
        //updateWrapper表示条件构造器
        cartMapper.update(cartTemp, updateWrapper);
    }

    @Override
    public void deleteCart(Cart cart) {
        //根据对象中的不为null的属性充当where条件
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);
        cartMapper.delete(queryWrapper);
    }

    /**
     * 数据库中是否已经存在该数据
     * 如果不存在，入库，否则新增
     */
    @Override
    public void saveCart(Cart cart) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", cart.getUserId())
                    .eq("item_id", cart.getItemId());
        Cart cartDB = cartMapper.selectOne(queryWrapper);
        if (cartDB == null) {
            //用户做新增操作
            cart.setCreated(new Date()).setUpdated(cart.getCreated());
            cartMapper.insert(cart);
        } else {
            //用户之前新增过记录 数量修改
            int num = cart.getNum() + cartDB.getNum();
            Cart cartTemp = new Cart();
            cartTemp.setNum(num).setId(cartDB.getId()).setUpdated(new Date());
            //根据对象中不为null的元素更新数据
            cartMapper.updateById(cartTemp);
        }
    }

}
