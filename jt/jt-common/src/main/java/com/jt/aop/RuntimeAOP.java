package com.jt.aop;

import com.jt.vo.SysResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice + @ResponseBody //定义为全局异常处理机制
@RestControllerAdvice //@ControllerAdvice + @ResponseBody
@Slf4j
public class RuntimeAOP {

    @ExceptionHandler(RuntimeException.class) //当前的AOP所拦截的异常类型
    public SysResult runtime(Exception exception){
        exception.printStackTrace(); //输出异常的信息
        return SysResult.fail();
    }
}
