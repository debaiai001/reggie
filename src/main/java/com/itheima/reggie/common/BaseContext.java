package com.itheima.reggie.common;

/**
 * @className: BaseContext
 * @description: 基于ThreadLocal封装工具类，用户保存和获取当前用户id
 * @author: Figure
 * @date: 2023/02/10 16:02
 **/
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
