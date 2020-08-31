package com.ilong.miaoshashop.service.impl;

import com.ilong.miaoshashop.dao.OrderDao;
import com.ilong.miaoshashop.dao.OrderMiaoshaDao;
import com.ilong.miaoshashop.entity.Order;
import com.ilong.miaoshashop.entity.OrderMiaosha;
import com.ilong.miaoshashop.entity.Product;
import com.ilong.miaoshashop.entity.User;
import com.ilong.miaoshashop.entity.vo.ProductVo;
import com.ilong.miaoshashop.redis.OrderKey;
import com.ilong.miaoshashop.redis.RedisService;
import com.ilong.miaoshashop.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;
import java.util.Date;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 20:12
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    RedisService redisService;

    @Autowired
    OrderMiaoshaDao orderMiaoshaDao;


    public OrderMiaosha getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        //return orderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid, ""+userId+"_"+goodsId, OrderMiaosha.class);
    }

    public Order getOrderById(long orderId) {
        return orderDao.getOne(orderId);
    }


    @Transactional
    public Order createMiaoshaOrder(User user, ProductVo productVo) {
        Order orderInfo = new Order();
        orderInfo.setCreated(new Date());
        orderInfo.setUpdated(new Date());
        orderInfo.setAddressId(1);
        orderInfo.setProductCount(1);
        orderInfo.setProductId(productVo.getId());
        orderInfo.setProductName(productVo.getProductName());
        orderInfo.setProductPrice(productVo.getMiaoshaPrice());
        orderInfo.setProductType(1);
        orderInfo.setPayStatus("5002");
        orderInfo.setUserId(user.getId());
        orderDao.save(orderInfo);
        OrderMiaosha miaoshaOrder = new OrderMiaosha();
        miaoshaOrder.setId(productVo.getId());
        miaoshaOrder.setProductId(orderInfo.getProductId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrder.setCreated(new Date());
        miaoshaOrder.setUpdated(new Date());
        orderMiaoshaDao.save(miaoshaOrder);

        redisService.set(OrderKey.getMiaoshaOrderByUidGid, ""+user.getId()+"_"+productVo.getId(), miaoshaOrder);

        return orderInfo;
    }

    public void deleteOrders() {
        orderDao.deleteOrder();
        orderMiaoshaDao.deleteOrderMiaosha();
    }


}
