package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult {

    private Integer status; //状态码信息 200成功 201失败
    private String msg; //后台服务器返回用户的提示信息
    private Object data; //后台返回的服务器对象

    //能否重载一些方法，简化用户调用
    public static SysResult fail(){
        return new SysResult(201,"服务器异常",null);
    }

    public static SysResult success(){
        return new SysResult(200, "业务调用成功", null);
    }

    public static SysResult success(Object data){
        return new SysResult(200, "业务调用成功", data);
    }

    public static SysResult success(String  msg,Object data){
        return new SysResult(200, msg, data);
    }
}
