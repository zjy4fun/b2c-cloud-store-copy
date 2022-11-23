package org.example.param;

import lombok.Data;

@Data
public class ProductParamsSearch {

    private String search;
    private Integer currentPage = 1;
    private Integer pageSize = 15;
}
