package com.jt.service;

import com.jt.anno.CacheFind;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private HttpClientService httpClientService;
    /**
     * 问题说明：
     *  1.jt-web服务器不能直接链接数据库，目的是为了降低代码的耦合性
     *  2.如果需要获取数据信息，则需要发起远程请求，访问jt-manage的数据
     *    jt-web 8092服务器向jt-manage 8091服务器发起请求之后获取业务数据
     *    协议支持：http/tcp/udp协议
     */

    //@CacheFind
    @Override
    public Item findItemById(Long itemId) {
        String url = "http://manage.jt.com:80/web/item/findItemById/" + itemId;
        String itemJSON = httpClientService.doGet(url);
        //将JSON串转化为item对象
        return ObjectMapperUtil.toObject(itemJSON,Item.class);
    }

    //@CacheFind
    @Override
    public ItemDesc findItemDescById(Long itemId) {
        String url = "http://manage.jt.com:80/web/item/findItemDescById/" + itemId;
        String itemDescJSON = httpClientService.doGet(url);
        return ObjectMapperUtil.toObject(itemDescJSON,ItemDesc.class);
    }
}
