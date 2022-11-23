package org.example.service;

import org.example.param.ProductHotParam;
import org.example.param.ProductParamInteger;
import org.example.utils.R;

public interface ProductService {
    R promo(String categoryName);

    /**
     * 多热门商品类别查询
     * @param productHotParam
     * @return
     */
    R hots(ProductHotParam productHotParam);

    /**
     * 查询类别商品集合
     * @return
     */
    R clist();

    /**
     * 类别商品查询，前端传递类别集合
     * @param productParamInteger
     * @return
     */
    Object byCategory(ProductParamInteger productParamInteger);

    /**
     * 查询全部商品
     * @param productParamInteger
     * @return
     */
    Object all(ProductParamInteger productParamInteger);
}
