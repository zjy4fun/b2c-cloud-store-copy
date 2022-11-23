package org.example.controller;

import org.example.param.ProductHotParam;
import org.example.service.CategoryService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 热门类别 id 查询
     * @param productHotParam
     * @param result
     * @return
     */
    @PostMapping("hots")
    public R hotsCategory(@RequestBody @Validated ProductHotParam productHotParam, BindingResult result) {
        if (result.hasErrors()) {
            return R.fail("类别集合查询失败！");
        }

        return categoryService.hotsCategory(productHotParam);
    }

    /**
     * 类别信息查询
     * @return
     */
    @GetMapping("list")
    public R list() {
        return categoryService.list();
    }
}
