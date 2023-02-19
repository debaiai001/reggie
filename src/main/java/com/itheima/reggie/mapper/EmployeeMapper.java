package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @className: EmployeeMapper
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/06 19:54
 **/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    //所有的CRUD都已经完成
    //不需要像以前一样配置一大堆文件：pojo-dao（连接mybatis，配置mapper.xml文件）==>service-controller
}
