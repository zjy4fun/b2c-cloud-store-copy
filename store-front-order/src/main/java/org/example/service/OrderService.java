package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.param.OrderParam;
import org.example.pojo.Order;

public interface OrderService extends IService<Order> {
    Object save(OrderParam orderParam);
}
