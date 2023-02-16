package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import org.springframework.stereotype.Service;

/**
 * @className: DishService
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/10 22:35
 **/
public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //根据id更新菜品信息和对应的口味信息
    public void updateWithFlavor(DishDto dishDto);
}
