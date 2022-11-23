package org.example.service;

import org.example.param.ProductHotParam;
import org.example.param.ProductParamInteger;
import org.example.param.ProductParamsSearch;
import org.example.pojo.Product;
import org.example.utils.R;

import java.util.List;

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

    /**
     * 查询商品详情
     * @param productID
     * @return
     */
    Object detail(Integer productID);

    /**
     * 查询商品图片
     * @param productID
     * @return
     */
    Object pictures(Integer productID);

    Object search(ProductParamsSearch productParamsSearch);

    /**
     * 查询全部商品信息
     * @return
     */
    List<Product> list();
}
