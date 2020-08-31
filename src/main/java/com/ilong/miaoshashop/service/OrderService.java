package com.ilong.miaoshashop.service;

import com.ilong.miaoshashop.entity.Order;
import com.ilong.miaoshashop.entity.OrderMiaosha;
import com.ilong.miaoshashop.entity.User;
import com.ilong.miaoshashop.entity.vo.ProductVo;
import org.springframework.stereotype.Service;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 20:07
 */

public interface OrderService {
    Order getOrderById(long orderId);
    OrderMiaosha getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);
    Order createMiaoshaOrder(User user, ProductVo productVo);
    void deleteOrders();
}
