package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @className: UserMapper
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/14 12:01
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
