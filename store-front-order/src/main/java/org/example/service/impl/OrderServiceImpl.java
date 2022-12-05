package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.clients.ProductClient;
import org.example.mapper.OrderMapper;
import org.example.param.OrderParam;
import org.example.param.ProductNumberParam;
import org.example.pojo.Order;
import org.example.service.OrderService;
import org.example.utils.R;
import org.example.vo.CartVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
            order.setOrder_id(ctime);
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
}
