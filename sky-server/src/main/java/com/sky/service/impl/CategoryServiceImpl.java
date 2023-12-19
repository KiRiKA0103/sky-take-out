package com.sky.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 分页查询分类
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        // 设置分页参数
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        Page<Category> p = categoryMapper.pageQuery(categoryPageQueryDTO.getName(), categoryPageQueryDTO.getType());

        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    @Override
    public List<Category> getByType(Integer type) {
        List<Category> list = categoryMapper.list(type);
        return list;
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void deleteById(Long id) {

        //查询当前分类是否关联了菜品或套餐，如果关联了就抛出业务异常
        Long count = dishMapper.countByCategoryId(id);
        if(count>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        count = setMealMapper.countByCategoryId(id);
        if(count>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.deleteById(id);
    }

    /**
     * 禁用启用分类
     * @param id
     * @param status
     */
    @Override
    public void changeStatus(Long id, Integer status) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.update(category);
    }

    /**
     * 修改分类信息
     * @param categoryDTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {

        Category category = Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .sort(categoryDTO.getSort())
                .build();

        categoryMapper.update(category);
    }

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {

        Category category = new Category();

        BeanUtils.copyProperties(categoryDTO, category);
        // 设置状态
        category.setStatus(StatusConstant.ENABLE);
        // 设置创建修改时间
        //category.setCreateTime(LocalDateTime.now());
        //category.setUpdateTime(LocalDateTime.now());
        // 设置创建人id和修改人id
        //category.setCreateUser(BaseContext.getCurrentId());
        //category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }
}
