package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @className: EmployeeController
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/06 21:27
 **/
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * @Author Figure
     * @Description 员工登录
     * @Date 21:45 2023/2/6
     * @Param [request, employee]
     * @return com.itheima.reggie.common.R<com.itheima.reggie.entity.Employee>
     **/
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1. 将提交的密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2. 将页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3. 如果没有查询到返回登录失败结果
        if (emp == null){
            return R.error("登录失败");
        }

        //4. 密码比对，如果不一致返回登陆失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //5. 查看员工状态，如果已禁用，返回员工已禁用结果
        if (emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //6. 登陆成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * @Author Figure
     * @Description 员工退出
     * @Date 22:23 2023/2/6
     * @Param [request]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清空session中保存的当前员工状态
        request.removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * @Author Figure
     * @Description 新增员工
     * @Date 15:00 2023/2/7
     * @Param [employee]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @PostMapping()
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息: {}", employee.toString());

        //设置初始密码123456，进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //公共字段自动填充，不再需要以下代码
        //设置创建和更新用户
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        //获取当前登录用户id,设置创建和更新用户
//        Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * @Author Figure
     * @Description 员工信息分页查询
     * @Date 20:59 2023/2/7
     * @Param [page, pageSize, name]
     * @return com.itheima.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     **/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){   //这里的形参名称与前端要一致
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件，姓名这种用模糊查询like，好过eq,若name不为空里面才有条件, 否则条件为空一切正常
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询, 结果会封装到pageInfo中，虽然传进queryWrapper，但里面不一定有条件
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * @Author Figure
     * @Description 根据id修改员工信息
     * @Date 22:37 2023/2/7
     * @Param [employee]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @PutMapping()
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为 {}" + id);

        //公共字段自动填充，不再需要以下代码
        //根据id修改用户，更新时间和更新人(尽管传入的employee只有部分属性有值，就是这些值不同的才进行更新)
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    /**
     * @Author Figure
     * @Description 根据id查询员工信息
     * @Date 16:43 2023/2/9
     * @Param [id]
     * @return com.itheima.reggie.common.R<com.itheima.reggie.entity.Employee>
     **/
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
