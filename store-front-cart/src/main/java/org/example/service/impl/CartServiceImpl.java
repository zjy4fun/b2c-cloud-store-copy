package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.clients.ProductClient;
import org.example.mapper.CartMapper;
import org.example.param.CartParam;
import org.example.param.ProductIdsParam;
import org.example.pojo.Cart;
import org.example.pojo.Product;
import org.example.service.CartService;
import org.example.utils.R;
import org.example.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private CartMapper cartMapper;
    /**
     * 添加购物车
     * @param cartParam
     * @return
     */
    @Override
    public R save(CartParam cartParam) {
        //查询关联的商品信息，复用商品集合查询
        List<Integer> ids = new ArrayList<>();
        ids.add(cartParam.getProductId());
        ProductIdsParam productIdsParam = new ProductIdsParam();
        productIdsParam.setProductIds(ids);
        List<Product> productList = productClient.ids(productIdsParam);

        if (productList == null || productList.size() == 0) {
            log.info("CartServiceImpl业务开始，商品被移除，无法添加！");
            return R.fail("商品已经被删除，无法添加");
        }
        //1. 检查是否已经达到最大库存
        Product product = productList.get(0);
        int productNum = product.getProductNum();
        if (productNum == 0) {
            R fail = R.fail("已经没有库存，无法购买");
            fail.setCode("003");
            return fail;
        }

        //2. 检查是不是第一次添加
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", cartParam.getUserId());
        queryWrapper.eq("product_id", cartParam.getProductId());
        Cart cart = cartMapper.selectOne(queryWrapper);
        if (cart != null) {
            //不是第一次，直接返回已经添加过即可
            //更新属性 +1
            cart.setNum(cart.getNum() + 1);
            cartMapper.updateById(cart);
            R ok = R.ok("商品已经在购物车上，数量+1！");
            ok.setCode("002");
            return ok;
        }

        //3. 第一次结果封装
        cart = new Cart();
        cart.setNum(1);
        cart.setProductId(cartParam.getProductId());
        cart.setUserId(cartParam.getUserId());

        cartMapper.insert(cart);

        //结果封装
        CartVo cartVo = new CartVo(product, cart);
        log.info("CartServiceImpl.save业务结束，结果:{}", cartVo);
        return R.ok(cartVo);
    }
}
