package org.example.controller;

import org.example.param.ProductHotParam;
import org.example.param.ProductParamInteger;
import org.example.param.ProductParamsSearch;
import org.example.param.ProductPromoParam;
import org.example.service.ProductService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/promo")
    public R promo(@RequestBody @Validated ProductPromoParam productPromoParam, BindingResult result){
        if (result.hasErrors()) {
            return R.fail("数据查询失败！");
        }
        return productService.promo(productPromoParam.getCategoryName());
    }

    @PostMapping("hots")
    public R hots(@RequestBody @Validated ProductHotParam productHotParam, BindingResult result) {
        if (result.hasErrors()) {
            return R.fail("数据查询失败！");
        }
        return productService.hots(productHotParam);
    }

    @PostMapping("/category/list")
    public R clist() {
        return productService.clist();
    }

    /**
     * 类别查询
     * @param productParamInteger
     * @return
     */
    @PostMapping("bycategory")
    public Object byCategory(@RequestBody ProductParamInteger productParamInteger) {
        return productService.byCategory(productParamInteger);
    }

    @PostMapping("all")
    public Object all(@RequestBody ProductParamInteger productParamInteger) {
        return productService.all(productParamInteger);
    }

    @PostMapping("detail")
    public Object detail(@RequestBody Map<String, Integer> param) {
        Integer productID = param.get("productID");
        return productService.detail(productID);
    }

    @PostMapping("pictures")
    public Object productPictures(@RequestBody Map<String, Integer> param) {
        Integer productID = param.get("productID");
        return productService.pictures(productID);
    }

    @PostMapping("search")
    public Object search(@RequestBody ProductParamsSearch productParamsSearch) {
        return productService.search(productParamsSearch);
    }
}
