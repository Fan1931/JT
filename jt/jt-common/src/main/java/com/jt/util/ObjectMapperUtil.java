package com.jt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

    /**
     * 该工具方法，实现对象与json数据的转化
     */

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 1.将对象转化为json
     */
    public static String toJSON(Object obj){
        try {
            String json = MAPPER.writeValueAsString(obj);
            return json;  //如果没有错返回json串
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 2.将json串转化为对象
     *  需求：要求自动转化数据的类型，用户传递什么类型，就实例化什么对象
     */
    public static <T> T toObject(String json,Class<T> targetClass){
        try {
            T t = MAPPER.readValue(json, targetClass);
            return t;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
