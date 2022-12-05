package org.example.clients;

import org.example.param.ProductParamsSearch;
import org.example.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "search-service")
public interface SearchClient {

    /**
     * 搜索服务 商品查询
     */
    @PostMapping("/search/product")
    R search(@RequestBody ProductParamsSearch productParamsSearch);
}
