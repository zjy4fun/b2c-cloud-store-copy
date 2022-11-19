package org.example.controller;

import org.example.service.CategoryService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/promo/{categoryName}")
    public R byName(@PathVariable String categoryName) {
        if (StringUtils.isEmpty(categoryName)) {
            return R.fail("类别名称为空，无法查询类别数据！");
        }
        return categoryService.byName(categoryName);
    }
}
