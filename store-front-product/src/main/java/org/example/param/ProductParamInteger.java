package org.example.param;

import lombok.Data;

import java.util.List;

@Data
public class ProductParamInteger {
    private List<Integer> categoryID;
    private int currentPage = 1;
    private int pageSize = 15;
}
