package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @className: CategoryMapper
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/10 20:22
 **/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
