package org.example.service;

import org.example.utils.R;

public interface CategoryService {
    /**
     * 根据类别名称查询类别对象
     * @param categoryName
     * @return
     */
    R byName(String categoryName);
}
