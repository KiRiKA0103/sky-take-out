package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetMealDishMapper {

    @Select("select count(*) from setmeal_dish where dish_id=#{id}")
    Long countByDishId(Long id);

    @Insert("insert into setmeal_dish(setmeal_id, dish_id, name, price, copies) " +
            "values (#{setmealId},#{dishId},#{name},#{price},#{copies})")
    void insert(SetmealDish setmealDish);
}
