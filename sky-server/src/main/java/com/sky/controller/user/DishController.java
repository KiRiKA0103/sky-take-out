package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    @Cacheable(cacheNames = "dishCache",key = "#categoryId")
    public Result<List<DishVO>> getList(Long categoryId){

//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        // 查询redis是否存在菜品数据
//        String key = "dish_"+categoryId;
//        List<DishVO> list = (List<DishVO>) valueOperations.get(key);
//        if(list != null && list.size()>0){
//            // 存在直接返回
//            return Result.success(list);
//        }
//        // 不存在,缓存到redis
//        list = dishService.getListWithFlavor(categoryId);
//        valueOperations.set(key,list);

        List<DishVO> list = dishService.getListWithFlavor(categoryId);

        return Result.success(list);
    }
}
