package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @className: CategoryController
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/10 20:28
 **/
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @Author Figure
     * @Description //TODO 保存新增分类
     * @Date 21:27 2023/2/10
     * @Param [category]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @PostMapping()
    public R<String> save(@RequestBody Category category){
        log.info("category: {}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * @Author Figure
     * @Description //TODO 分类分页查询
     * @Date 22:04 2023/2/10
     * @Param [page, pageSize]
     * @return com.itheima.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     **/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        log.info("page = {}, pageSize = {}", page, pageSize);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        //添加排序条件,根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //执行查询, 结果会封装到pageInfo中
        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * @Author Figure
     * @Description //TODO 根据id删除分类
     * @Date 22:26 2023/2/10
     * @Param [id]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @DeleteMapping()
    public R<String> delete(Long id){
        log.info("删除分类，id为{}", id);

        categoryService.remove(id);
        return  R.success("分类信息删除成功");
    }

    /**
     * @Author Figure
     * @Description //TODO 根据id修改分类信息
     * @Date 11:12 2023/2/11
     * @Param [category]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @PutMapping()
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息, {}", category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * @Author Figure
     * @Description //TODO 根据条件查询分类数据
     * @Date 14:51 2023/2/12
     * @Param [category]
     * @return com.itheima.reggie.common.R<java.util.List<com.itheima.reggie.entity.Category>>
     **/
    @GetMapping("/list")
    public R<List<Category>> list(Category category){   //用实体来封装而不是String，有助于后期根据其它条件查询
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
