package org.example.param;

import lombok.Data;

@Data
public class ProductParamsSearch {

    private String search;
    private Integer currentPage = 1;
    private Integer pageSize = 15;

    /**
     * 计算分页起始值
     * @return
     */
    public int getFrom() {
        return (currentPage - 1) * pageSize;
    }

    /**
     * 返回查询值
     * @return
     */
    public int getSize() {
        return pageSize;
    }
}
