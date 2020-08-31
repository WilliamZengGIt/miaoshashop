package com.ilong.miaoshashop.controller;

import com.ilong.miaoshashop.entity.Order;
import com.ilong.miaoshashop.entity.User;
import com.ilong.miaoshashop.entity.vo.OrderDetailVo;
import com.ilong.miaoshashop.entity.vo.ProductVo;
import com.ilong.miaoshashop.redis.RedisService;
import com.ilong.miaoshashop.result.CodeMsg;
import com.ilong.miaoshashop.result.Result;
import com.ilong.miaoshashop.service.OrderService;
import com.ilong.miaoshashop.service.ProductService;
import com.ilong.miaoshashop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 20:02
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, User user,
                                      @RequestParam("orderId") long orderId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        Order order = orderService.getOrderById(orderId);
        if(order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        Long productId = order.getProductId();
        ProductVo product = productService.getProductVoByProductId(productId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setProductVo(product);
        return Result.success(vo);
    }
}
