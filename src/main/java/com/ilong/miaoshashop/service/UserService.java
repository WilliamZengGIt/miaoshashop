package com.ilong.miaoshashop.service;

import com.ilong.miaoshashop.entity.User;
import com.ilong.miaoshashop.entity.vo.LoginVo;
import com.ilong.miaoshashop.entity.vo.RegisterVo;
import com.ilong.miaoshashop.result.Result;

import javax.servlet.http.HttpServletResponse;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-14 20:04
 */
public interface UserService {
    Result<String> register(RegisterVo registerVo);

    String login(HttpServletResponse response, LoginVo loginVo);

    User getByToken(HttpServletResponse response,String token);
    public static final String COOKI_NAME_TOKEN = "token";
}
