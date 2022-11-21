package org.example.service;

import org.example.param.ProductHotParam;
import org.example.utils.R;

public interface CategoryService {
    /**
     * 根据类别名称查询类别对象
     * @param categoryName
     * @return
     */
    R byName(String categoryName);

    /**
     * 根据传入的类别名称集合返回类别对应的 id 集合
     * @param productHotParam
     * @return
     */
    R hotsCategory(ProductHotParam productHotParam);

    /**
     * 返回所有类别信息
     * @return
     */
    R list();
}
