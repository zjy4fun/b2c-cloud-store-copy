package org.example.service;

import org.example.param.CartParam;
import org.example.utils.R;

public interface CartService {
    R save(CartParam cartParam);

    R list(CartParam cartParam);

    R update(CartParam cartParam);
}
