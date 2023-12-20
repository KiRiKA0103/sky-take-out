package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private FlavorMapper flavorMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增菜品和口味
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        // 向菜品表插入1条数据
        dishMapper.insert(dish);

        // 获取生成的主键id
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // 向口味表插入n条数据
            flavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        Page<Dish> p = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(p.getTotal(),p.getResult());
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {

        DishVO dishVO = new DishVO();
        Dish dish = dishMapper.queryById(id);

        BeanUtils.copyProperties(dish,dishVO);

        Long categoryId = dish.getCategoryId();
        Long dishId = dish.getId();

        String categoryName = categoryMapper.queryNameById(categoryId);
        List<DishFlavor> flavors = flavorMapper.queryByDishId(dishId);

        dishVO.setCategoryName(categoryName);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getList(Long categoryId) {
        return dishMapper.queryList(categoryId);
    }

    /**
     * 菜品起售停售
     * @param status
     * @param id
     */
    @Override
    public void changeStatus(Integer status, Long id) {
        Dish dish = Dish.builder().id(id).status(status).build();
        dishMapper.update(dish);
    }

    /**
     * 修改菜品和口味
     * @param dishDTO
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        // 修改菜品表
        dishMapper.update(dish);
        // 获取菜品id
        Long dishId = dishDTO.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();

        List<Long> ids = new ArrayList<>();
        ids.add(dishId);
        // 删除菜品口味
        flavorMapper.deleteBatch(ids);

        if(flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // 向口味表插入n条数据
            flavorMapper.insertBatch(flavors);
        }


    }


    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {

        dishMapper.deleteBatch(ids);

        flavorMapper.deleteBatch(ids);

    }
}
