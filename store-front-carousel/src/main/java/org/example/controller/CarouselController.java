package org.example.controller;

import org.example.service.CarouselService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    /**
     * 查询轮播图数据，只要优先级最高的 6 条
     * @return
     */
    @PostMapping("list")
    public R list() {
        return carouselService.list();
    }
}
