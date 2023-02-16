package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @className: DishServiceImpl
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/10 22:36
 **/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * @Author Figure
     * @Description //TODO 新增菜品,同时保存对应的口味数据
     * @Date 15:48 2023/2/12
     * @Param [dishDto]
     * @return void
     **/
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId((dishId));
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味表数据到菜品口味表dish_flavor,saveBatch批量保存
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * @Author Figure
     * @Description //TODO 根据id查询菜品信息和对应的口味信息
     * @Date 20:47 2023/2/12
     * @Param [id]
     * @return com.itheima.reggie.dto.DishDto
     **/
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜谱基本信息
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询菜品对应的口味信息，从dish_flvaor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * @Author Figure
     * @Description //TODO 根据id更新菜品信息和对应的口味信息
     * @Date 21:24 2023/2/12
     * @Param [dishDto]
     * @return void
     **/
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新Dish表基本信息,不一样也没关系，会更新相同的字段
        this.updateById(dishDto);

        //清除当前菜品对应口味数据--dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //添加提交过来的口味数据--dish_flavor表的save操作
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        //封装的flavors只有name和value字段，缺失dish_id字段
        flavors = flavors.stream().map((item) -> {
            item.setDishId((dishId));
            return item;
        }).collect(Collectors.toList());
        //批量保存
        dishFlavorService.saveBatch(flavors);
    }
}
