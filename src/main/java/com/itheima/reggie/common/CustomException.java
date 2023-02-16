package com.itheima.reggie.common;

/**
 * @className: CustomException
 * @description: TODO 自定义业务异常类
 * @author: Figure
 * @date: 2023/02/10 23:19
 **/
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
