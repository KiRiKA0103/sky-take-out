package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.NotSelectedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 设置启售状态
        setmeal.setStatus(StatusConstant.DISABLE);
        // System.out.println(setmeal);
        // 插入套餐数据
        setMealMapper.insert(setmeal);
        // 获取套餐id
        Long setMealId = setmeal.getId();

        // 获取套餐菜品对应关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        if (setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> {
                // System.out.println(setmealDish);
                // 设置套餐id
                setmealDish.setSetmealId(setMealId);
            });
            setMealDishMapper.insertBatch(setmealDishes);
        }

    }

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> p = setMealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        // 根据id查询套餐
        Setmeal setmeal = setMealMapper.queryById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);

        Long setMealId = setmeal.getId();
        Long categoryId = setmeal.getCategoryId();

        // 查询套餐分类名称和菜品
        String categoryName = categoryMapper.queryNameById(categoryId);
        List<SetmealDish> setmealDishes = setMealDishMapper.queryBysetMealId(setMealId);
        // 设置套餐分类名称和菜品
        setmealVO.setCategoryName(categoryName);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 套餐启售停售
     * @param status
     * @param id
     */
    @Override
    public void changeStatus(Integer status, Long id) {
        // 套餐中是否有未起售的菜品
        if(status==StatusConstant.ENABLE){
            Long count = setMealDishMapper.countDishStatus(StatusConstant.DISABLE,id);
            if(count>0){
                // 套餐中有未起售的菜品,不可启售
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }

        Setmeal setmeal = Setmeal.builder().id(id).status(status).build();
        setMealMapper.update(setmeal);
    }

    /**
     * 修改套餐和关联菜品
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 修改套餐
        setMealMapper.update(setmeal);
        // 获取套餐id
        Long setMealId = setmealDTO.getId();
        // 获取套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        // 根据套餐id删除套餐菜品关系
        List<Long> setMealIds = new ArrayList<>();
        setMealIds.add(setMealId);
        setMealDishMapper.deleteBatch(setMealIds);
        // 重新插入套餐菜品关系
        if (setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> {
                // 设置套餐id
                setmealDish.setSetmealId(setMealId);
            });
            setMealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 套餐是否在售
        for (Long id : ids) {
            Setmeal setmeal = setMealMapper.queryById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                // 在售状态,不可删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        if (ids.size() > 0) {
            // 删除套餐
            setMealMapper.deleteBatch(ids);

            // 删除套餐菜品对应关系
            setMealDishMapper.deleteBatch(ids);
        }else {
            throw new NotSelectedException(MessageConstant.SETMEAL_NOT_SELECTED);
        }
    }
}
