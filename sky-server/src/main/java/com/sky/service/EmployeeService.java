package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import java.time.LocalDate;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);


    /**
     * 分页查询员工数据
     * @param name
     * @param page
     * @param pageSize
     */
    PageResult pageQuery(String name, Integer page, Integer pageSize);

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    void changeStatus(Integer status, Long id);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Employee getById(Long id);


    /**
     * 修改员工
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}
