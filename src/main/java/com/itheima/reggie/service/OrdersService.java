package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;

/**
 * @className: OrdersService
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/15 21:43
 **/
public interface OrdersService extends IService<Orders> {

    //用户下单
    public void submit(Orders orders);
}
