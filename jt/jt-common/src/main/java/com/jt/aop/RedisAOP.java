package com.jt.aop;

import com.jt.anno.CacheFind;
import com.jt.util.ObjectMapperUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

@Aspect //标识切面
@Component  //将对象交给容器管理
public class RedisAOP {

    //某些业务并不需要缓存
    @Autowired(required = false)
    //private Jedis jedis;  //表示单台redis
    //private ShardedJedis jedis;  //redis分片
    //private Jedis jedis;  //从哨兵中注入
    private JedisCluster jedis;  //redis集群
    /**
     * 业务描述：利用@CacheFind注解，实现redis缓存操作
     *   1.动态生成key，包名.类名.方法名::第一个参数
     * 技巧：利用该配置，可以直接为参数添加指定的注解的对象，目的是为了将来获取注解的属性方便
     */
    @Around("@annotation(cacheFind)")
    public Object around(ProceedingJoinPoint joinPoint, CacheFind cacheFind){

        //1.动态生成key
        String key = getKey(joinPoint,cacheFind);
        System.out.println(key);
        //2.利用redis查询数据
        String json = jedis.get(key);

        Object returnobject = null;
        //3.判断redis中的数据是否有值
        if (StringUtils.isEmpty(json)){
            //如果json为null则表示第一次查询，应该查询数据库
            try {
                returnobject = joinPoint.proceed();//让目标方法执行 查询数据库

                //将数据获取之后保存到redis，将数据转化JSON
                String objJSON = ObjectMapperUtil.toJSON(returnobject);
                if (cacheFind.seconds()>0) {
                    //表示赋值操作有超时时间
                    jedis.setex(key, cacheFind.seconds(), objJSON);
                }else {
                    jedis.set(key, objJSON);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            System.out.println("AOP查询数据库");
        }else {
            //redis不为空，直接返回数据
            Class<?> targetClass = getClass(joinPoint);
            returnobject = ObjectMapperUtil.toObject(json, targetClass);
            System.out.println("AOP查询缓存");
        }
        return returnobject;
    }

    //动态获取返回值类型
    private Class<?> getClass(ProceedingJoinPoint joinPoint) {
        //专门获取方法对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature(); //源码中的子接口，现阶段记着
        return methodSignature.getReturnType();
    }


    /**
     * 如果用户由key使用自己的key，如果没有，生成key
     * @param joinPoint
     * @return
     */
    private String getKey(ProceedingJoinPoint joinPoint,CacheFind cacheFind) {
        if (StringUtils.isEmpty(cacheFind.key())){
            return cacheFind.key();
        }
        //1.获取对象的类路径 包名.类名
        String className = joinPoint.getSignature().getDeclaringTypeName();
        //2.获取方法名
        String methodName = joinPoint.getSignature().getName();
        //3.获取第一个参数
        String firstArgs = joinPoint.getArgs()[0].toString();
        return className+"."+methodName+"::"+firstArgs;
    }


//    /**
//     * 切入点写法：
//     *   1.bean(itemCatServiceImpl)  //针对单个对象
//     *   2.within(com.jt.service.*)  //针对多个对象
//     *   3.execution(返回值类型 包名.类名.方法名(参数列表))
//     */
//    //@Pointcut("bean(itemCatServiceImpl)")
//    //@Pointcut("within(com.jt.service.*)")
//    @Pointcut("execution(* com.jt.service..*.*(..))")
//    public void pointCut(){}
//
//    @Before("pointCut()")  //定义前置通知
//    public void before(){
//        System.out.println("我是前置通知");
//    }
//
//    @AfterReturning("pointCut()")
//    public void afterReturning(){
//        System.out.println("我是后置通知");
//    }
//
//    @AfterThrowing(pointcut = "pointCut()",throwing = "throwable")
//    public void afterThrow(Throwable throwable){
//        System.out.println(throwable.getMessage());
//        System.out.println("我是异常通知");
//    }
//
//    @After("pointCut()")//最终通知，无论什么时候，都会执行的通知
//    public void after(){
//        System.out.println("我是最终通知");
//    }
//
//    /**
//     * 环绕通知是五大类型中最重要的配置
//     * joinPoint,环绕通知中，必须位于首位
//     */
//    @Around("pointCut()")
//    public Object around(ProceedingJoinPoint joinPoint){
//        try {
//            System.out.println("我是环绕通知1");
//            Object obj = joinPoint.proceed(); //让目标方法执行
//            System.out.println("我是环绕通知2");
//            return obj;
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//            throw new RuntimeException();
//        }
//    }

}
