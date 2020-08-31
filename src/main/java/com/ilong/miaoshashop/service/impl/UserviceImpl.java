package com.ilong.miaoshashop.service.impl;

import com.ilong.miaoshashop.dao.UserDao;
import com.ilong.miaoshashop.entity.User;
import com.ilong.miaoshashop.entity.vo.LoginVo;
import com.ilong.miaoshashop.entity.vo.RegisterVo;
import com.ilong.miaoshashop.exception.GlobalException;
import com.ilong.miaoshashop.redis.MiaoshaUserKey;
import com.ilong.miaoshashop.redis.RedisService;
import com.ilong.miaoshashop.result.CodeMsg;
import com.ilong.miaoshashop.result.Result;
import com.ilong.miaoshashop.service.UserService;
import com.ilong.miaoshashop.util.MD5Util;
import com.ilong.miaoshashop.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-14 20:04
 */
@Service
public class UserviceImpl implements UserService {





    @Autowired
    UserDao userDao;

    @Autowired
    RedisService redisService;




    @Override
    public Result<String> register(RegisterVo registerVo) {
        if (registerVo==null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile=registerVo.getUserTel();
        String formPass=registerVo.getUserPwd();
        //判断手机号是否存在
        Optional<User> user = userDao.findByUserTel(mobile);
        if (user.isPresent()){
            throw new GlobalException(CodeMsg.MOBILE_IS_EXIST);
        }
        User newuser = new User();
        newuser.setUserTel(mobile);
        newuser.setUpdated(new Date());
        newuser.setCreated(new Date());
        newuser.setUserSalt("1a2b3c");
        newuser.setUserPwd(MD5Util.inputPassToDbPass(formPass, newuser.getUserSalt()));
        userDao.save(newuser);
        return Result.success("注册成功");
    }

    public User getById(String mobile) {
        //将moblie转为long 类型 以做为redis的key
        long id=Long.parseLong(mobile);
        //取缓存
        User user = redisService.get(MiaoshaUserKey.getById, ""+id, User.class);
        if(user != null) {
            return user;
        }
        //取数据库
       Optional<User> user1 = userDao.findByUserTel(mobile);
        if(user1.isPresent()) {
            redisService.set(MiaoshaUserKey.getById, ""+id, user1);
        }
        return user;
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    @Override
    public String login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getUserTel();
        String formPass = loginVo.getUserPwd();
        //判断手机号是否存在
        User user = getById(mobile);
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getUserPwd();
        String saltDB = user.getUserSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token	 = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    @Override
    public User getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(MiaoshaUserKey.token, token, User.class);
        //延长有效期
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }
}
