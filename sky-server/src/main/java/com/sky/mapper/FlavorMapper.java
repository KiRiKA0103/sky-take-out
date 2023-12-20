package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FlavorMapper {
    void insertBatch(List<DishFlavor> flavors);

    void deleteBatch(List<Long> dishIds);

    @Select("select * from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> queryByDishId(Long dishId);

}
