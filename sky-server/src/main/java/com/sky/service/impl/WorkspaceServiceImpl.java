package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.entity.Setmeal;
import com.sky.mapper.*;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public BusinessDataVO getBusinessData() {

        LocalDate today = LocalDate.now();
        LocalDateTime beginTime = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(today, LocalTime.MAX);


        Map map = new HashMap();
        map.put("begin", beginTime);
        map.put("end", endTime);

        // 新增用户数
        Integer newUsers = userMapper.countByMap(map);
        Integer totalOrderCount = orderMapper.countByMap(map);
        map.put("status", Orders.COMPLETED);
        // 营业额
        Double turnover = orderMapper.sumByMap(map);
        turnover = turnover == null ? 0.0 : turnover;
        // 有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);

        // 平均客单价
        Double unitPrice = 0.0;
        if (validOrderCount != 0) {
            unitPrice = turnover / validOrderCount;
        }

        // 订单完成率
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .newUsers(newUsers)
                .orderCompletionRate(orderCompletionRate)
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .unitPrice(unitPrice)
                .build();

        return businessDataVO;
    }

    @Override
    public SetmealOverViewVO getOverviewSetmeals() {

        return setMealMapper.countStatus();
    }

    @Override
    public DishOverViewVO getOverviewDishes() {
        return dishMapper.countStatus();
    }

    @Override
    public OrderOverViewVO getOverviewOrders() {

        // 全部订单
        Integer allOrders = orderMapper.countStatus(null);
        // 待接单数量
        Integer waitingOrders = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        // 待派送数量
        Integer deliveredOrders = orderMapper.countStatus(Orders.CONFIRMED);
        // 已取消数量
        Integer cancelledOrders = orderMapper.countStatus(Orders.COMPLETED);
        // 已完成数量
        Integer completedOrders = orderMapper.countStatus(Orders.CANCELLED);

        OrderOverViewVO orderOverViewVO = OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();

        return orderOverViewVO;
    }
}
