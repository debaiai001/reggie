package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @className: OrdersMapper
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/15 21:42
 **/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
