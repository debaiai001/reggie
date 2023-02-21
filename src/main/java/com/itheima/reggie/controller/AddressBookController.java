package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @className: AddressBookController
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/14 15:58
 **/
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * @Author Figure
     * @Description //TODO 新增地址
     * @Date 16:29 2023/2/14
     * @Param [addressBook]
     * @return com.itheima.reggie.common.R<com.itheima.reggie.entity.AddressBook>
     **/
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * @Author Figure
     * @Description //TODO 查询本用户的所有地址列表
     * @Date 16:29 2023/2/14
     * @Param []
     * @return com.itheima.reggie.common.R<java.util.List<com.itheima.reggie.entity.AddressBook>>
     **/
    @GetMapping("/list")
    public R<List<AddressBook>> list(){
        Long currentId = BaseContext.getCurrentId();

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, currentId);
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * @Author Figure
     * @Description //TODO 设置默认地址
     * @Date 16:54 2023/2/14
     * @Param [addressBook]
     * @return com.itheima.reggie.common.R<com.itheima.reggie.entity.AddressBook>
     **/
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        Long currentId = BaseContext.getCurrentId();

        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, currentId);
        updateWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(updateWrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }

    /**
     * @Author Figure
     * @Description //TODO 根据id获取地址信息
     * @Date 17:05 2023/2/14
     * @Param [id]
     * @return com.itheima.reggie.common.R<com.itheima.reggie.entity.AddressBook>
     **/
    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    /**
     * @Author Figure
     * @Description //TODO 更新地址信息
     * @Date 17:05 2023/2/14
     * @Param [addressBook]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @PutMapping()
    public R<String> update(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);

        return R.success("更新成功");
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }
}
