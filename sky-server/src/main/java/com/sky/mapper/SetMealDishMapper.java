package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    void insertBatch(List<SetmealDish> setmealDishes);

    @Select("select count(*) from setmeal_dish where dish_id=#{id}")
    Long countByDishId(Long id);

    @Select("select * from setmeal_dish where setmeal_id=#{setMealId}")
    List<SetmealDish> queryBysetMealId(Long setMealId);


    void deleteBatch(List<Long> setMealIds);

    @Select("select count(*) from setmeal_dish sd left join dish d on d.id = sd.dish_id where sd.setmeal_id=#{id} and status=#{status}")
    Long countDishStatus(Integer status,Long id);
}
