package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/workspace")
@Api(tags = "工作台相关接口")
@Slf4j
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;


    @GetMapping("/businessData")
    @ApiOperation("查询今日运营数据")
    public Result<BusinessDataVO> businessData(){
        log.info("查询今日运营数据");

        LocalDate today = LocalDate.now();

        BusinessDataVO businessDataVO = workspaceService.getBusinessData(today,today);

        return Result.success(businessDataVO);
    }

    @GetMapping("/overviewSetmeals")
    @ApiOperation("查询套餐总览")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        log.info("查询套餐总览");

        SetmealOverViewVO setmealOverViewVO = workspaceService.getOverviewSetmeals();

        return Result.success(setmealOverViewVO);
    }


    @GetMapping("/overviewDishes")
    @ApiOperation("查询菜品总览")
    public Result<DishOverViewVO> overviewDishes(){
        log.info("查询菜品总览");

        DishOverViewVO dishOverViewVO = workspaceService.getOverviewDishes();

        return Result.success(dishOverViewVO);
    }

    @GetMapping("/overviewOrders")
    @ApiOperation("查询订单管理数据")
    public Result<OrderOverViewVO> overviewOrders(){
        log.info("查询订单管理数据");

        OrderOverViewVO orderOverViewVO = workspaceService.getOverviewOrders();

        return Result.success(orderOverViewVO);
    }

}
