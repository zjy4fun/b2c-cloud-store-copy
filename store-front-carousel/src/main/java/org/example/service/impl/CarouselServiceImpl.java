package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.CarouselMapper;
import org.example.pojo.Carousel;
import org.example.service.CarouselService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    /**
     * 查询优先级最高的六条轮播图数据
     *      按照优先级查询数据库数据
     *      使用 stream 流进行内存数据切割，保留 6 条数据
     * @return
     */
    @Override
    public R list() {
        QueryWrapper<Carousel> carouselQueryWrapper = new QueryWrapper<>();
        carouselQueryWrapper.orderByDesc("priority");
        List<Carousel> list = carouselMapper.selectList(carouselQueryWrapper);
        // jdk 1.8 stream 切割
        List<Carousel> collect = list.stream().limit(6).collect(Collectors.toList());
        R ok = R.ok(collect);

        log.info("CarouselServiceImpl.list()业务结束，结果:{}", ok);
        return ok;
    }
}
