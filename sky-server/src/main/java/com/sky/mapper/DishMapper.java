package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id统计菜品数量
     * @param id
     * @return
     */
    @Select("select count(*) from dish where category_id=#{id}")
    Long countByCategoryId(Long id);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Select("select * from dish where id=#{id}")
    Dish queryById(Long id);

    /**
     * 根据分类id查询菜品
     * @param dish
     * @return
     */
    @Select("select * from dish where category_id=#{categoryId} and status=#{status}")
    List<Dish> queryList(Dish dish);

    /**
     * 修改菜品
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    @Select("select * from dish d left join setmeal_dish sd on d.id = sd.dish_id where sd.setmeal_id=#{id}")
    List<DishItemVO> getDishList(Long id);
}
