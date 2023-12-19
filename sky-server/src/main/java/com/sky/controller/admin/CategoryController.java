package com.sky.controller.admin;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询分类
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询分类")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO){

        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据类型查询分类")
    public Result<List<Category>> getByType(Integer type){

        List<Category> list= categoryService.getByType(type);

        return Result.success(list);
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "根据id删除分类")
    public Result deleteById(Long id){

        categoryService.deleteById(id);

        return Result.success();
    }

    /**
     * 禁用启用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "禁用启用分类")
    public Result changeStatus(@PathVariable Integer status,Long id){

        categoryService.changeStatus(id,status);

        return Result.success();
    }

    /**
     * 修改分类信息
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改分类信息")
    public Result update(@RequestBody CategoryDTO categoryDTO){

        categoryService.update(categoryDTO);

        return Result.success();
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO){

        categoryService.save(categoryDTO);

        return Result.success();
    }

}
