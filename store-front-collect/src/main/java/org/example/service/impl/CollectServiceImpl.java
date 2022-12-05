package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.CollectMapper;
import org.example.param.CollectParam;
import org.example.service.CollectService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.pojo.Collect;

@Slf4j
@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

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
}
