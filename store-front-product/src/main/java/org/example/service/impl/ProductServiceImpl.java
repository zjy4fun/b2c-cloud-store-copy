package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.clients.CategoryClient;
import org.example.clients.SearchClient;
import org.example.mapper.PictureMapper;
import org.example.mapper.ProductMapper;
import org.example.param.*;
import org.example.pojo.Picture;
import org.example.pojo.Product;
import org.example.service.ProductService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl  extends ServiceImpl<ProductMapper,Product> implements ProductService {

    //引入 feign 客户端需要在启动类添加配置注解
    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SearchClient searchClient;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PictureMapper pictureMapper;

    @Cacheable(value = "list.product", key = "#categoryName")
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
     * 1. 调用类别服务
     * 2. 类别集合 id 查询商品
     * 3. 结果集封装即可
     *
     * @param productHotParam 类别名称集合
     * @return r
     */
    @Cacheable(value = "list.product", key = "#productHotParam.categoryName")
    @Override
    public R hots(ProductHotParam productHotParam) {
        R r = categoryClient.hots(productHotParam);
        if (r.getCode().equals(R.FAIL_CODE)) {
            log.info("ProductServiceImpl.hots业务结束，结果:{}", r.getMsg());
            return r;
        }
        List<Object> ids = (List<Object>) r.getData();

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

    @Cacheable(value = "list.category", key = "#root.methodName")
    @Override
    public R clist() {
        R r = categoryClient.list();
        log.info("ProductServiceImpl.clist业务结束，结果：{}", r);
        return r;
    }

    @Cacheable(value = "list.product", key = "#productParamInteger.categoryID+" + "'-'+#productParamInteger.currentPage+" + "'-'+#productParamInteger.pageSize")
    @Override
    public Object byCategory(ProductParamInteger productParamInteger) {
        //1. 拆分请求参数
        List<Integer> categoryID = productParamInteger.getCategoryID();
        int currentPage = productParamInteger.getCurrentPage();
        int pageSize = productParamInteger.getPageSize();
        //2. 请求条件封装
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        if (categoryID != null && categoryID.size() > 0) {
            productQueryWrapper.in("category_id", categoryID);
        }
        IPage<Product> page = new Page<>(currentPage, pageSize);
        //3. 数据查询
        IPage<Product> iPage = productMapper.selectPage(page, productQueryWrapper);
        //4. 结果封装
        List<Product> productList = iPage.getRecords();
        long total = iPage.getTotal();

        R ok = R.ok(null, productList, total);

        log.info("ProductServiceImpl.byCategory业务结束，结果：{}", ok);
        return ok;
    }

    @Cacheable(value = "list.product", key = "#productParamInteger.currentPage+" + "'-'+#productParamInteger.pageSize")
    @Override
    public Object all(ProductParamInteger productParamInteger) {
        return byCategory(productParamInteger);
    }

    @Cacheable(value = "product", key = "#productID")
    @Override
    public Object detail(Integer productID) {
        Product product = productMapper.selectById(productID);
        R ok = R.ok(product);
        log.info("ProductServiceImpl.detail业务结束，结果：{}", ok);

        return ok;
    }

    @Cacheable(value = "picture", key = "#productID")
    @Override
    public Object pictures(Integer productID) {
        //参数封装
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productID);
        //数据库查询
        List<Picture> pictureList = pictureMapper.selectList(queryWrapper);
        R r = R.ok(pictureList);

        log.info("ProductServiceImpl.pictures业务结束，结果：{}", r);

        return r;
    }

    @Cacheable(value = "list.product", key = "#productParamsSearch.search+'-'+#productParamsSearch.pageSize+'-'+#productParamsSearch.currentPage")
    @Override
    public Object search(ProductParamsSearch productParamsSearch) {
        R r = searchClient.search(productParamsSearch);
        log.info("ProductServiceImpl.search业务结束，结果:{}", r);

        return r;
    }

    @Override
    public List<Product> list() {
        List<Product> products = productMapper.selectList(null);
        return products;
    }

    /**
     * 根据传入的id，查询商品的集合
     * @param productIdsParam
     * @return
     */
    @Cacheable(value = "list.product", key = "#productIdsParam.productIds")
    @Override
    public List<Product> ids(ProductIdsParam productIdsParam) {
        List<Integer> productIds = productIdsParam.getProductIds();
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("product_id", productIds);
        List<Product> productList = productMapper.selectList(queryWrapper);
        return productList;
    }

    /**
     * 修改商品库存
     * @param productNumberParams
     */
    @Override
    @Transactional
    public void batchNumber(List<ProductNumberParam> productNumberParams) {
        /**
         * 将 productNumberParams 转成 map
         * 使用 id 作为 key，item 做值， 比较相邻的两次 key，如果相同，去掉重读
         */
        Map<Integer, ProductNumberParam> productNumberParamMap = productNumberParams.stream()
                .collect(Collectors.toMap(ProductNumberParam::getProductId, v -> v));

        //封装商品集合
        Set<Integer> productIds = productNumberParamMap.keySet();

        List<Product> productList = baseMapper.selectBatchIds(productIds);

        for(Product product : productList){
            //设置新库存
            product.setProductNum(product.getProductNum() - productNumberParamMap.get(product.getProductId()).getProductNum());
            //设置销售量
            product.setProductSales(product.getProductSales() + productNumberParamMap.get(product.getProductId()).getProductNum());
        }

        //批量数据更新
        this.updateBatchById(productList);
    }
}
