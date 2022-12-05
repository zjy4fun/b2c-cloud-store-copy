package org.example.clients;

import org.example.param.ProductIdsParam;
import org.example.pojo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {
    /**
     * 商品全部数据调用
     * @return
     */
    @GetMapping("/product/list")
    List<Product> list();

    /**
     * 收藏服务调用
     * @param productIdsParam
     * @return
     */
    @PostMapping("/product/ids")
    List<Product> ids(@RequestBody ProductIdsParam productIdsParam);
}
