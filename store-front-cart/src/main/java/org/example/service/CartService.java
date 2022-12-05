package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.param.CartParam;
import org.example.pojo.Cart;
import org.example.utils.R;

public interface CartService extends IService<Cart> {
    R save(CartParam cartParam);

    R list(CartParam cartParam);

    R update(CartParam cartParam);

    R remove(CartParam cartParam);
}
