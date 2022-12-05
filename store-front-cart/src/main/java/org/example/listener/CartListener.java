package org.example.listener;

import org.example.service.CartService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartListener {

    @Autowired
    private CartService cartService;

    /**
     * 购物车数据清空监听
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "clear.queue"),
            exchange = @Exchange("topic.ex"),
            key = "clear.cart"
    ))
    public void subNumber(List<Integer> cartIds) {
        System.out.println("CartListener.subNumber");
        System.out.println("cartIds = " + cartIds);

        cartService.removeBatchByIds(cartIds);
    }
}
