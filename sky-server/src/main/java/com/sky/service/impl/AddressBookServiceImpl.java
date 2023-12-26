package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.controller.user.AddressBookController;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;
    @Override
    public void save(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(StatusConstant.DISABLE);
        addressBookMapper.insert(addressBook);
    }

    @Override
    @Transactional
    public void setDefault(AddressBook addressBook) {
        // 将当前用户所有地址修改未非默认地址
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        addressBook.setIsDefault(StatusConstant.DISABLE);
        addressBookMapper.updateByUserId(addressBook);

        // 将当前地址修改未默认地址
        addressBook.setIsDefault(StatusConstant.ENABLE);
        addressBookMapper.update(addressBook);
    }

    @Override
    public AddressBook getById(Long id) {

        AddressBook addressBook = addressBookMapper.getById(id);
        return addressBook;
    }

    @Override
    public AddressBook getDefault() {
        AddressBook addressBook = new AddressBook();
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        addressBook.setIsDefault(StatusConstant.ENABLE);
        List<AddressBook> bookList = addressBookMapper.list(addressBook);

        return bookList.get(0);
    }

    @Override
    public List<AddressBook> getList() {
        AddressBook addressBook = new AddressBook();
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);

        List<AddressBook> bookList = addressBookMapper.list(addressBook);

        return bookList;
    }

    @Override
    public void delete(Long id) {
        addressBookMapper.delete(id);
    }

    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }
}
