package com.jt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestObjectMapper {

    /**
     * 该API实现将Java对象转化为JSON字符串
     * 1.实例化工具API对象
     * 2.利用api方法实现数据转化
     */

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testObjectJSON() throws JsonProcessingException {
        //java对象
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L)
                .setItemDesc("您好redis！！！")
                .setCreated(new Date())
                .setUpdated(itemDesc.getCreated());
        //Java对象转JSON
        //对象转化为JSON时，调用的是get方法
        String json = MAPPER.writeValueAsString(itemDesc);
        System.out.println(json);

        //能否将JSON串，转化为对象  需要提供对象的类型
        //JSON串转化为对象时，调用set方法
        ItemDesc itemDesc2 = MAPPER.readValue(json, ItemDesc.class);
        //toString方法只会添加自己的属性
        System.out.println(itemDesc2);
    }

    @Test
    public void testListJSON() throws JsonProcessingException {
        //java对象
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L)
                .setItemDesc("您好redis！！！")
                .setCreated(new Date())
                .setUpdated(itemDesc.getCreated());

        ItemDesc itemDesc2 = new ItemDesc();
        itemDesc.setItemId(100L)
                .setItemDesc("您好redis！！！")
                .setCreated(new Date())
                .setUpdated(itemDesc.getCreated());

        //封装Java对象
        List<ItemDesc> list = new ArrayList<>();
        list.add(itemDesc);
        list.add(itemDesc2);

        //将对象转化为list集合
        String json = MAPPER.writeValueAsString(list);
        System.out.println(json);

        //将json串转化为list集合
        List<ItemDesc> itemList = MAPPER.readValue(json, list.getClass());
        System.out.println(itemList);
    }

    /**
     * 测试objectMapper工具方法
     */
    @Test
    public void testObjectMapperUtil(){
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L)
                .setItemDesc("您好redis！！！")
                .setCreated(new Date())
                .setUpdated(itemDesc.getCreated());
        String json = ObjectMapperUtil.toJSON(itemDesc);
        System.out.println(json);

        ItemDesc itemDesc2 = ObjectMapperUtil.toObject(json, ItemDesc.class);
        System.out.println(itemDesc2);
    }
}
