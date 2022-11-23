package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.param.ProductParamsSearch;
import org.example.utils.R;

public interface SearchService {
    R search(ProductParamsSearch productParamsSearch) throws JsonProcessingException;
}
