package com.ilong.miaoshashop.controller;

import com.ilong.miaoshashop.access.AccessLimit;
import com.ilong.miaoshashop.dao.OrderMiaoshaDao;
import com.ilong.miaoshashop.entity.Order;
import com.ilong.miaoshashop.entity.OrderMiaosha;
import com.ilong.miaoshashop.entity.User;
import com.ilong.miaoshashop.entity.vo.ProductVo;
import com.ilong.miaoshashop.rabbitmq.MQSender;
import com.ilong.miaoshashop.rabbitmq.MiaoshaMessage;
import com.ilong.miaoshashop.redis.GoodsKey;
import com.ilong.miaoshashop.redis.MiaoshaKey;
import com.ilong.miaoshashop.redis.OrderKey;
import com.ilong.miaoshashop.redis.RedisService;
import com.ilong.miaoshashop.result.CodeMsg;
import com.ilong.miaoshashop.result.Result;
import com.ilong.miaoshashop.service.MiaoshaService;
import com.ilong.miaoshashop.service.OrderService;
import com.ilong.miaoshashop.service.ProductService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 20:49
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    UserController userService;

    @Autowired
    RedisService redisService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderMiaoshaDao orderMiaoshaDao;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    MQSender sender;

    private HashMap<Long, Boolean> localOverMap =  new HashMap<Long, Boolean>();

    /**
     * 系统初始化
     * */
    public void afterPropertiesSet() throws Exception {
        List<ProductVo> productsList = productService.listProductVo();
        if(productsList == null) {
            return;
        }
        for(ProductVo productVo : productsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+productVo.getId(), productVo.getProductStock());
            localOverMap.put(productVo.getId(), false);
        }
    }

    @RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<ProductVo> productsList = productService.listProductVo();
        for(ProductVo productVo : productsList) {
            productVo.setProductStock(10);
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+productVo.getId(), 10);
            localOverMap.put(productVo.getId(), false);
        }
        redisService.delete(OrderKey.getMiaoshaOrderByUidGid);
        redisService.delete(MiaoshaKey.isGoodsOver);
        miaoshaService.reset(productsList);
        return Result.success(true);
    }

    /**
     * QPS:1306
     * 5000 * 10
     * QPS: 2114
     * */
    @RequestMapping(value="/{path}/do_miaosha", method=RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, User user,
                                   @RequestParam("goodsId")long goodsId,
                                   @PathVariable("path") String path) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, ""+goodsId);//10
        if(stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        OrderMiaosha order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setProductId(goodsId);
        sender.sendMiaoshaMessage(mm);
        return Result.success(0);//排队中

    	/*//判断库存
        ProductVo productVo = productService.getProductVoByProductId(goodsId);//10个商品，req1 req2
        Integer productStock = productVo.getProductStock();

    	if(productStock <= 0) {
    		return Result.error(CodeMsg.MIAO_SHA_OVER);
    	}
    	//判断是否已经秒杀到了
        OrderMiaosha order1= orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);

    	if(order1!= null) {
    		return Result.error(CodeMsg.REPEATE_MIAOSHA);
    	}
    	//减库存 下订单 写入秒杀订单
        Order orderInfo = miaoshaService.miaosha(user, productVo);

        return Result.success(orderInfo);
*/
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,User user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result  =miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @RequestMapping(value="/path", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, User user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value="verifyCode", defaultValue="0")int verifyCode
    ) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path  =miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }


    @RequestMapping(value="/verifyCode", method= RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, User user,
                                              @RequestParam("goodsId")long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image  = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }
}

