package com.ilong.miaoshashop.service;

import com.ilong.miaoshashop.entity.Order;
import com.ilong.miaoshashop.entity.OrderMiaosha;
import com.ilong.miaoshashop.entity.User;
import com.ilong.miaoshashop.entity.vo.ProductVo;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 20:56
 */
public interface MiaoshaService {

    void reset(List<ProductVo> ProductVoList);

    boolean checkPath(User user, long id, String  path);

    long getMiaoshaResult(Integer userId,Long productId);

    boolean checkVerifyCode(User user,long productid,int verifyCode);

    String createMiaoshaPath(User user,long productId);

    BufferedImage createVerifyCode(User user, long productId);



   Order miaosha(User user,ProductVo productVo);

}
