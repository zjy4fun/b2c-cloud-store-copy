package org.example.service;

import org.example.param.ProductHotParam;
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
}
