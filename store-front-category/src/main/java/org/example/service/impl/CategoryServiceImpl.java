package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.CategoryMapper;
import org.example.param.ProductHotParam;
import org.example.pojo.Category;
import org.example.service.CategoryService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public R hotsCategory(ProductHotParam productHotParam) {
        //1. 封装查询参数
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("category_name", productHotParam.getCategoryName());
        queryWrapper.select("category_id");

        //2. 查询数据库
        List<Object> ids = categoryMapper.selectObjs(queryWrapper);
        R ok = R.ok("类别集合查询成功", ids);
        log.info("CategoryServiceImpl.hotsCategory业务结束，结果：{}", ok);
        return ok;
    }

    /**
     * 返回所有类别信息
     * @return
     */
    @Override
    public R list() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        R ok = R.ok("类别信息查询成功", categories);
        log.info("CategoryServiceImpl.list业务结束，结果:{}", ok);
        return ok;
    }
}
