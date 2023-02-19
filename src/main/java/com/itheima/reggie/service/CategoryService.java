package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * @className: CategoryService
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/10 20:23
 **/
public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
