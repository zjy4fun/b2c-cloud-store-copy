package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.clients.CategoryClient;
import org.example.mapper.ProductMapper;
import org.example.param.ProductHotParam;
import org.example.pojo.Product;
import org.example.service.ProductService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    //引入 feign 客户端需要在启动类添加配置注解
    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private ProductMapper productMapper;
    @Override
    public R promo(String categoryName) {
        R r = categoryClient.byName(categoryName);
        if (r.getCode().equals(R.FAIL_CODE)) {
            log.info("ProductServiceImpl.promo 业务结束，结果:{}", "类别查询失败！");
            return r;
        }
        // 类别服务中 data = category --- feign {json}  ----- product服务 LinkedHashMap jackson

        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) r.getData();
        Integer categoryId = (Integer) map.get("category_id");
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        queryWrapper.orderByDesc("product_sales");

        IPage<Product> page = new Page<>(1, 7);

        page = productMapper.selectPage(page, queryWrapper);

        List<Product> productList = page.getRecords();//指定页的数据
        long total = page.getTotal();//获取总条数

        log.info("ProductServiceImpl.promo业务结束，结果:{}", productList);

        return R.ok("数据查询成功", productList);
    }

    /**
     * 多类别热门商品查询，根据类别名称集合，至多查询 7 条
     *    1. 调用类别服务
     *    2. 类别集合 id 查询商品
     *    3. 结果集封装即可
     * @param productHotParam 类别名称集合
     * @return r
     */
    @Override
    public R hots(ProductHotParam productHotParam) {
        R r = categoryClient.hots(productHotParam);
        if (r.getCode().equals(R.FAIL_CODE)) {
            log.info("ProductServiceImpl.hots业务结束，结果:{}", r.getMsg());
            return r;
        }
        List<Object> ids = (List<Object>)r.getData();

        //进行商品数据查询
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("category_id", ids);
        queryWrapper.orderByDesc("product_sales");

        IPage<Product> page = new Page<>(1, 7);

        page = productMapper.selectPage(page, queryWrapper);

        List<Product> records = page.getRecords();

        R ok = R.ok("多类别热门商品查询成功！", records);

        log.info("ProductServiceImpl.hots业务结束，结果：{}", ok);

        return ok;
    }

    @Override
    public R clist() {
        R r = categoryClient.list();
        log.info("ProductServiceImpl.clist业务结束，结果：{}", r);
        return r;
    }
}
