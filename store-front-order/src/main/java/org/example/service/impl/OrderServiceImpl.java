package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.clients.ProductClient;
import org.example.mapper.OrderMapper;
import org.example.param.OrderParam;
import org.example.param.ProductIdsParam;
import org.example.param.ProductNumberParam;
import org.example.pojo.Order;
import org.example.pojo.Product;
import org.example.service.OrderService;
import org.example.utils.R;
import org.example.vo.CartVo;
import org.example.vo.OrderVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ProductClient productClient;

    /**
     * 消息队列发送
     * @param orderParam
     * @return
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 订单保存业务
     * 库存和购物车使用mq异步，避免分布式事务
     * @param orderParam
     * @return
     */
    @Override
    @Transactional
    public Object save(OrderParam orderParam) {
        //修改购物车的参数
        List<Integer> cartIds = new ArrayList<>();
        //修改批量插入数据库的参数
        List<Order> orderList = new ArrayList<>();
        //商品修改库存参数集合
        List<ProductNumberParam> productNumberParamList = new ArrayList<>();
        Integer userId = orderParam.getUserId();
        List<CartVo> products = orderParam.getProducts();
        //封装order实体类集合
        //统一生成订单编号和创建时间
        //使用时间戳 + 做订单编号和事件
        long ctime = System.currentTimeMillis();

        for(CartVo cartVo : products) {
            //进行购物车订单保存
            cartIds.add(cartVo.getId());
            //订单信息保存
            Order order = new Order();
            order.setOrderId(ctime);
            order.setUserId(userId);
            order.setOrderTime(ctime);
            order.setProductId(cartVo.getProductID());
            order.setProductNum(cartVo.getNum());
            order.setProductPrice(cartVo.getPrice());
            orderList.add(order);//添加用户信息

            //修改信息存储
            ProductNumberParam productNumberParam = new ProductNumberParam();
            productNumberParam.setProductId(cartVo.getProductID());
            productNumberParam.setProductNum(cartVo.getNum());
            productNumberParamList.add(productNumberParam);//添加集合
        }

        //批量数据插入
        this.saveBatch(orderList);

        //修改商品库存 [product-service] [异步通知]
        /**
         * 交换机：topic.ex
         * routingkey: sub.number
         * 消息：商品 id 和 减库存数据集合
         */
        rabbitTemplate.convertAndSend("topic.ex", "sub.number", productNumberParamList);
        //清空对应购物车数据即可 [注意：不是清空用户所有的购物车数据] [cart-service] [异步通知]
        /**
         * 交换机：topic.ex
         * routingkey: clear.cart
         * 消息：要清空的购物车 id 集合
         */
        rabbitTemplate.convertAndSend("topic.ex", "clear.cart", cartIds);

        R ok = R.ok("订单生成成功！");
        log.info("OrderServiceImpl.save业务结束，结果:{}", ok);
        return ok;
    }

    @Override
    public Object list(OrderParam orderParam) {
        Integer userId = orderParam.getUserId();
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id", userId);
        List<Order> orderList = this.list(orderQueryWrapper);

        Set<Integer> productIds = new HashSet<>();
        for(Order order : orderList) {
            productIds.add(order.getProductId());
        }

        //数据按订单分组
        Map<Long, List<Order>> listMap = orderList.stream().collect(Collectors.groupingBy(Order::getOrderId));

        //结果集封装
        ProductIdsParam productIdsParam = new ProductIdsParam();
        productIdsParam.setProductIds(new ArrayList<>(productIds));

        List<Product> productList = productClient.ids(productIdsParam);

        Map<Integer, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getProductId, v -> v));

        List<List<OrderVo>> result = new ArrayList<>();
        for(List<Order> orders : listMap.values()) {
            List<OrderVo> orderVos = new ArrayList<>();
            for(Order order : orders) {
                OrderVo orderVo = new OrderVo();
                Product product = productMap.get(order.getProductId());
                orderVo.setProductName(product.getProductName());
                orderVo.setProductPicture(product.getProductPicture());
                orderVo.setId(order.getId());
                orderVo.setOrderId(order.getOrderId());
                orderVo.setOrderTime(order.getOrderTime());
                orderVo.setProductNum(order.getProductNum());
                orderVo.setProductId(order.getProductId());
                orderVo.setProductPrice(order.getProductPrice());
                orderVo.setUserId(order.getUserId());
                orderVos.add(orderVo);
            }
            result.add(orderVos);
        }

        R ok = R.ok(result);
        log.info("OrderServiceImpl.list业务结束，结果:{}", ok);
        return ok;
    }
}
