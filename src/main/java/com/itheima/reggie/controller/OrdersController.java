package com.itheima.reggie.controller;

import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: OrdersController
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/15 21:44
 **/
@Slf4j
@RequestMapping("/order")
@RestController
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /**
     * @Author Figure
     * @Description //TODO 用户下单
     * @Date 22:05 2023/2/15
     * @Param [orders]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据: {}", orders);
        ordersService.submit(orders);
        return R.success("提交订单成功");
    }
}
