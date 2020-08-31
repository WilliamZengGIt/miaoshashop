package com.ilong.miaoshashop.controller;

import com.ilong.miaoshashop.entity.Product;
import com.ilong.miaoshashop.entity.User;
import com.ilong.miaoshashop.entity.vo.ProductDetailVo;
import com.ilong.miaoshashop.entity.vo.ProductVo;
import com.ilong.miaoshashop.redis.GoodsKey;
import com.ilong.miaoshashop.redis.RedisService;
import com.ilong.miaoshashop.result.Result;
import com.ilong.miaoshashop.service.UserService;
import com.ilong.miaoshashop.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-15 15:07
 */
@Controller
@RequestMapping("/products")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    ProductService productService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;


    @RequestMapping(value="/to_list", produces="text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model,User user) {

        model.addAttribute("user", user);

        //取缓存
//    	String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
//    	if(!StringUtils.isEmpty(html)) {
//    		return html;
//    	}
        List<ProductVo> productList = productService.listProductVo();
        model.addAttribute("productList", productList);
//    	 return "goods_list";
        SpringWebContext ctx = new SpringWebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
        //手动渲染
        String html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;

    }


    @RequestMapping(value="/to_detail2/{productId}",produces="text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model,User user,
                          @PathVariable("productId")long productId) {
        model.addAttribute("user", user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+productId, String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        //手动渲染
        ProductVo product = productService.getProductVoByProductId(productId);
        model.addAttribute("product", product);

        long startAt = product.getStartDate().getTime();
        long endAt = product.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
//        return "goods_detail";

        SpringWebContext ctx = new SpringWebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, ""+productId, html);
        }
        return html;
    }

    @RequestMapping(value="/detail/{productId}")
    @ResponseBody
    public Result<ProductDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, User user,
                                          @PathVariable("productId")long productId) {
        ProductVo product = productService.getProductVoByProductId(productId);
        long startAt = product.getStartDate().getTime();
        long endAt = product.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        ProductDetailVo vo = new ProductDetailVo();
        vo.setProductVo(product);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }


}
