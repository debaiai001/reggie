package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @className: DishController
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/12 14:28
 **/
@RestController
@RequestMapping("dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @Author Figure
     * @Description //TODO 新增菜品
     * @Date 15:37 2023/2/12
     * @Param [dishDto]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @PostMapping()
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);

        //清除所有菜品数据
        //Set keys = redisTemplate.keys("dish_*");
        //redisTemplate.delete(keys);

        //动态构造key
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

        return R.success("新增菜品成功");
    }

    /**
     * @Author Figure
     * @Description //TODO 菜品分类查询
     * @Date 18:02 2023/2/12
     * @Param [page, pageSize, name]
     * @return com.itheima.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     **/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name!=null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId(); //分类ID
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * @Author Figure
     * @Description //TODO 根据id查询菜品信息和对应的口味信息
     * @Date 20:43 2023/2/12
     * @Param [id]
     * @return com.itheima.reggie.common.R<com.itheima.reggie.dto.DishDto>
     **/
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        log.info("根据id查询菜品信息");

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * @Author Figure
     * @Description //TODO 修改菜品
     * @Date 21:22 2023/2/12
     * @Param [dishDto]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @PutMapping()
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);

        //清除所有菜品数据
        //Set keys = redisTemplate.keys("dish_*");
        //redisTemplate.delete(keys);

        //动态构造key
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        
        return R.success("新增菜品成功");
    }

    /**
     * @Author Figure
     * @Description //TODO 根据条件查询对应的菜品数据
     * @Date 23:46 2023/2/14
     * @Param [dish]
     * @return com.itheima.reggie.common.R<java.util.List<com.itheima.reggie.dto.DishDto>>
     **/
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dtoList = null;

        //动态构造key
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();

        //先从Redis中获取缓存数据
        dtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        //如果存在直接返回无需查询数据库
        if (dtoList != null){
            return R.success(dtoList);
        }

        //构造条件查询
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1(启售)的菜品
        queryWrapper.eq(Dish::getStatus, 1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        //新添加代码，方法返回类型也从Dish改为DishDto
        dtoList = list.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(DishFlavor::getDishId, id);
            List<DishFlavor> flavors = dishFlavorService.list(queryWrapper2);

            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());

        //如果不存在需要查询数据库，将查到的菜品数据缓存到Redis,这里设置60分钟缓存失效
        redisTemplate.opsForValue().set(key, dtoList, 60, TimeUnit.MINUTES);

        return R.success(dtoList);
    }
}
