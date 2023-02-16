package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.mapper.AdderssBookMapper;
import com.itheima.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @className: AddressBookServiceImpl
 * @description: TODO 类描述
 * @author: Figure
 * @date: 2023/02/14 15:56
 **/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AdderssBookMapper, AddressBook> implements AddressBookService {
}
