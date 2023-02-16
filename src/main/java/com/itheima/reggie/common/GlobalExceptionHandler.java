package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @className: GlobalExceptionHandler
 * @description: 全局异常处理
 * @author: Figure
 * @date: 2023/02/07 15:29
 **/
@ControllerAdvice(annotations = {RestController.class, Controller.class})//处理带有该注解的controller
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * @Author Figure
     * @Description SQLIntegrityConstraintViolationException异常处理方法
     * @Date 15:35 2023/2/7
     * @Param [ex]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//处理该异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")){
            String[] splits = ex.getMessage().split(" ");
            String msg = splits[2] + "已存在";
            return R.error(msg);
        }

        return R.error("未知异常");
    }

    /**
     * @Author Figure
     * @Description //TODO 处理业务异常CustomException
     * @Date 23:28 2023/2/10
     * @Param [ex]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @ExceptionHandler(CustomException.class)//处理该异常
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());

        return R.error(ex.getMessage());
    }
}
