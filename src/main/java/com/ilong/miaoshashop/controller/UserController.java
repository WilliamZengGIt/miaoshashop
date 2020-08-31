package com.ilong.miaoshashop.controller;

import com.ilong.miaoshashop.entity.User;
import com.ilong.miaoshashop.entity.vo.LoginVo;
import com.ilong.miaoshashop.entity.vo.RegisterVo;
import com.ilong.miaoshashop.redis.RedisService;
import com.ilong.miaoshashop.result.Result;
import com.ilong.miaoshashop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 *
 *
 * @author long
 * @date 2020-01-14 20:21
 */
@Controller
@RequestMapping("/user")
public class UserController  {
    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_register")
    public String toRegister() {
        return "register";
    }

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_register")
    @ResponseBody
    public Result<String> doregier(@Valid RegisterVo registerVo){
        log.info(registerVo.toString());
        return userService.register(registerVo);
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(Model model,HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());

        //登录
        String token = userService.login(response, loginVo);
        User user = userService.getByToken(response, token);
        model.addAttribute("user", user);
        return Result.success(token);

    }

}
