package org.example.controlle;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.param.ProductParamsSearch;
import org.example.service.SearchService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("product")
    public R productList(@RequestBody ProductParamsSearch productParamsSearch) throws JsonProcessingException {
        return searchService.search(productParamsSearch);
    }
}
