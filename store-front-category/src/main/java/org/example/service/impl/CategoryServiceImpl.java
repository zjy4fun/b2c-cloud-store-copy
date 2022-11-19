package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.CategoryMapper;
import org.example.pojo.Category;
import org.example.service.CategoryService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public R byName(String categoryName) {
        //1. 封装查询参数
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("category_name", categoryName);
        log.info("categoryName: " + categoryName);
        //2. 查询数据库
        Category category = categoryMapper.selectOne(categoryQueryWrapper);
        //3. 结果封装
        if (category == null) {
            log.info("CategoryServiceImpl.byName业务结束，结果: 类别查询失败");
            return R.fail("类别查询失败！");
        }
        log.info("CategoryServiceImpl.byName业务结束，结果: 类别查询成功");
        return R.ok("类别查询成功", category);
    }
}
