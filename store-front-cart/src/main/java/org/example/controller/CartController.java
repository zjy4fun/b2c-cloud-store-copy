package org.example.controller;

import org.example.param.CartParam;
import org.example.service.CartService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @PostMapping("save")
    public R save(@RequestBody CartParam cartParam) {
        return cartService.save(cartParam);
    }

    @PostMapping("list")
    public R list(@RequestBody CartParam cartParam) {
        return cartService.list(cartParam);
    }

    @PostMapping("update")
    public R update(@RequestBody CartParam cartParam) {
        return cartService.update(cartParam);
    }

    @PostMapping("remove")
    public R remove(@RequestBody CartParam cartParam) {
        return cartService.remove(cartParam);
    }
}
