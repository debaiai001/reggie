package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.mapper.OrdersDetailMapper;
import com.itheima.reggie.service.OrdersDetailService;
import org.springframework.stereotype.Service;

/**
 * @className: OrdersDetailServiceImpl
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/15 21:46
 **/
@Service
public class OrdersDetailServiceImpl extends ServiceImpl<OrdersDetailMapper, OrderDetail> implements OrdersDetailService{
}
