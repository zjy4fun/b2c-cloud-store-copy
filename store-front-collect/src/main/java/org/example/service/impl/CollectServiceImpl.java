package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.clients.ProductClient;
import org.example.mapper.CollectMapper;
import org.example.param.CollectParam;
import org.example.param.ProductIdsParam;
import org.example.pojo.Product;
import org.example.service.CollectService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.pojo.Collect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private ProductClient productClient;
    /**
     * 收藏保存服务
     * @param collectParam
     * @return
     */
    @Override
    public Object save(CollectParam collectParam) {
        // 分解参数
        Integer userId = collectParam.getUserId();
        Integer productId = collectParam.getProductId();
        //数据库查询
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("product_id", productId);
        Long count = collectMapper.selectCount(queryWrapper);
        if(count > 0) {
            log.info("CollectServiceImpl.save 业务结束吗，结果:{}", count);
            return R.fail("商品已在收藏夹！无需二次添加！");
        }
        //实体类封装
        Collect collect = new Collect();
        collect.setProductId(productId);
        collect.setUserId(userId);
        collect.setCollectTime(System.currentTimeMillis());
        //数据库插入
        int rows = collectMapper.insert(collect);
        //结果封装
        return R.ok("商品添加成功！");
    }

    /**
     * 查询收藏列表
     * @param collectParam
     * @return
     */
    @Override
    public Object list(CollectParam collectParam) {
        Integer userId = collectParam.getUserId();
        //查询商品id
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userId);
        queryWrapper.select("product_id");
        List<Object> list = collectMapper.selectObjs(queryWrapper);

        //结果封装
        Integer[] idsArray = list.toArray(new Integer[]{});
        List<Integer> ids = new ArrayList<>();
        ids = Arrays.asList(idsArray);
        if (ids.size() == 0) {
            log.info("CollectServiceImpl.list业务结束，结果：{}");
            return R.ok(ids);
        }

        //调用商品服务
        ProductIdsParam productIdsParam = new ProductIdsParam();
        productIdsParam.setProductIds(ids);
        List<Product> productList = productClient.ids(productIdsParam);

        //结果封装
        return R.ok(productList);
    }
}
