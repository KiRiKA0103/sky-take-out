package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status) {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        valueOperations.set("SHOP_STATUS", status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus() {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        Integer shopStatus = (Integer) valueOperations.get("SHOP_STATUS");
        return Result.success(shopStatus);
    }
}
