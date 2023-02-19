package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @className: OrdersDetailMapper
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/15 21:45
 **/
@Mapper
public interface OrdersDetailMapper extends BaseMapper<OrderDetail> {
}
