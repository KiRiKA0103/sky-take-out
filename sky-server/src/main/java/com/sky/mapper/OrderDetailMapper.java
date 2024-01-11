package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderDetailMapper {

    @Insert("insert into order_detail(name, image, order_id, dish_id, setmeal_id, dish_flavor, amount) " +
            "values(#{name}, #{image}, #{orderId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{amount}) ")
    void insert(OrderDetail orderDetail);

    void insertBatch(List<OrderDetail> detailList);

    @Select("select * from order_detail where order_id=#{id}")
    List<OrderDetail> getByOrderId(Long id);

    List<GoodsSalesDTO> getSalesTop(LocalDateTime beginTime, LocalDateTime endTime);
}
