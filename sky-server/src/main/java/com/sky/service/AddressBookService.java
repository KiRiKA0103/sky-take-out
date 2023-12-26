package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void save(AddressBook addressBook);

    void setDefault(AddressBook addressBook);

    AddressBook getById(Long id);

    AddressBook getDefault();

    List<AddressBook> getList();

    void delete(Long id);

    void update(AddressBook addressBook);
}
