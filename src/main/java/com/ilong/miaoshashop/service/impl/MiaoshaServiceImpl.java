package com.ilong.miaoshashop.service.impl;

import com.ilong.miaoshashop.entity.Order;
import com.ilong.miaoshashop.entity.OrderMiaosha;
import com.ilong.miaoshashop.entity.User;
import com.ilong.miaoshashop.entity.vo.ProductVo;
import com.ilong.miaoshashop.rabbitmq.MQSender;
import com.ilong.miaoshashop.redis.MiaoshaKey;
import com.ilong.miaoshashop.redis.RedisService;
import com.ilong.miaoshashop.service.MiaoshaService;
import com.ilong.miaoshashop.service.OrderService;
import com.ilong.miaoshashop.service.ProductService;
import com.ilong.miaoshashop.service.UserService;
import com.ilong.miaoshashop.util.MD5Util;
import com.ilong.miaoshashop.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 21:31
 */
@Service
public class MiaoshaServiceImpl implements MiaoshaService {
    @Autowired
    ProductService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;



    @Override
    public void reset(List<ProductVo> ProductVoList) {
        goodsService.resetStock(ProductVoList);
        orderService.deleteOrders();
    }

    @Override
    public boolean checkPath(User user, long id, String path) {
        if(user == null || path == null) {
            return false;
        }
        String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, ""+user.getId() + "_"+ id, String.class);
        return path.equals(pathOld);

    }

    @Override
    public long getMiaoshaResult(Integer userId, Long productId) {
        OrderMiaosha order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, productId);
        if(order != null) {//秒杀成功
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(productId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    @Override
    public boolean checkVerifyCode(User user, long productid, int verifyCode) {
        if(user == null || productid <=0) {
            return false;
        }
        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+productid, Integer.class);
        if(codeOld == null || codeOld - verifyCode != 0 ) {
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+productid);
        return true;
    }

    @Override
    public String createMiaoshaPath(User user, long productId) {
        if(user == null || productId <=0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(MiaoshaKey.getMiaoshaPath, ""+user.getId() + "_"+ productId, str);
        return str;
    }

    @Override
    public BufferedImage createVerifyCode(User user, long productId) {
        if(user == null || productId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+productId, rnd);
        //输出图片
        return image;
    }

    @Transactional
    @Override
    public Order miaosha(User user, ProductVo productVo) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(productVo);
        if(success) {
            //order_info maiosha_order
            return orderService.createMiaoshaOrder(user, productVo);
        }else {
            setGoodsOver(productVo.getId());
            return null;
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};

    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }
}
