package org.example.clients;

import org.example.pojo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {
    /**
     * 商品全部数据调用
     * @return
     */
    @GetMapping("/product/list")
    List<Product> list();
}
