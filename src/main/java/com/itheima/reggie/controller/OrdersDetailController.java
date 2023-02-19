package com.itheima.reggie.controller;

import com.itheima.reggie.service.OrdersDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: OrdersDetailController
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/15 21:48
 **/
@Slf4j
@RestController
@RequestMapping("/ordersDetail")
public class OrdersDetailController {

    @Autowired
    private OrdersDetailService ordersDetailService;


}
