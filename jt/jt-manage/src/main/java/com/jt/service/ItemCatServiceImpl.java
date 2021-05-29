package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.anno.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private ItemCatMapper itemCatMapper;
    //注入一个可以为null的对象时最好添加此操作，除非自己手动调用
    @Autowired(required = false)
    private Jedis jedis;

    @Override
    public String findItemCatName(Long itemCatId) {
        return itemCatMapper.selectById(itemCatId).getName();
    }

    /**
     * 业务分析：
     *   商品分类查询是根据父级商品分类，查询子级的商品分类信息
     *   sql:select * from tb_item_cat where parent_id = 0;
     *   根据sql返回值List<itemCat>集合信息
     *   页面展现时，需要将List<itemCat>转化为List<EasyUITree>对象
     * 业务需求：
     *   需要将redis的商品分类信息利用redis进行缓冲处理
     * 缓冲分析：
     *   redis保存数据的结构key：value结构
     *   1.尽可能保证key唯一  包名.类名.方法名::第一个参数
     *   2.Java对象转化为String类型  Java对象~~~JSON~~~String
     * 缓冲业务实现：
     *   1.准备查询redis的key
     *   2.根据key查询redis缓存
     *   3.没有数据 第一次查询
     *     用户应该查询数据库，获取数据库信息，将数据保存到redis
     *   4.有数据  不是第一次查询
     *     可以直接通过缓冲获取数据，返回给用户，节省了查询数据库的时间
     *
     *  将业务和缓存操作写在一起，耦合性太高，不便于维护
     */
    @Override
    public List<EasyUITree> findItemCatListByCache(Long parentId) {
        Long startTime = System.currentTimeMillis();
        //1.准备动态变化的key
        String key = "com.jt.service.ItemCatServiceImp.findItemCatListByCache::"+parentId;
        String json = jedis.get(key);
        List<EasyUITree> list = new ArrayList<>();
        //2.判断当前数据是否为空
        if (StringUtils.isEmpty(json)) {
            list = findEasyUITreeByParentId(parentId);
            Long endTime = System.currentTimeMillis();
            System.out.println("查询数据库时间"+(endTime-startTime)+"毫秒");
            String jsonData = ObjectMapperUtil.toJSON(list);
            jedis.set(key, jsonData);
            System.out.println("查询数据库");
        } else {
            //缓存中有数据，可以将数据转化为对象
            list = ObjectMapperUtil.toObject(json, list.getClass());
            Long endTime = System.currentTimeMillis();
            System.out.println("查询缓存时间"+(endTime-startTime)+"毫秒");
        }
        return list;
    }

    @Override
    @CacheFind
    public List<EasyUITree> findEasyUITreeByParentId(Long parentId) {
        //查询数据库动态获取POJO对象
        List<ItemCat> itemCatList = getItemCatList(parentId);
        //将POJO转化为VO
        List<EasyUITree> treeList = new ArrayList<>(itemCatList.size());
        for (ItemCat itemCat:itemCatList){
            //如果是父级则是closed，否则open
            String state = itemCat.getIsParent() ? "closed" : "open";
            EasyUITree easyUITree = new EasyUITree();
            easyUITree.setId(itemCat.getId())
                    .setText(itemCat.getName())
                    .setState(state);
            treeList.add(easyUITree);
        }
        return treeList;
    }

    private List<ItemCat> getItemCatList(Long parentId) {
        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<ItemCat>();
        queryWrapper.eq("parent_id", parentId);
        return itemCatMapper.selectList(queryWrapper);
    }
}
