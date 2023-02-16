package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @className: MyMetaObjectHandler
 * @description: 自定义元数据处理器
 * @author: Figure
 * @date: 2023/02/10 14:52
 **/
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * @Author Figure
     * @Description 插入操作自动填充
     * @Date 15:25 2023/2/10
     * @Param [metaObject]
     * @return void
     **/
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]...");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    /**
     * @Author Figure
     * @Description 更新数据自动填充
     * @Date 15:26 2023/2/10
     * @Param [metaObject]
     * @return void
     **/
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]...");
        log.info(metaObject.toString());
        long id = Thread.currentThread().getId();
        log.info("线程id为 {}" + id);
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
