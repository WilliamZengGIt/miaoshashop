package com.ilong.miaoshashop.dao;

import com.ilong.miaoshashop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-14 20:00
 */
public interface UserDao extends JpaRepository<User,Integer> {
    //根据用户名查询用户信息
    public User findByUserName(String userName);
    //根据电话号码查询用户信息
    public Optional<User> findByUserTel (String userTel);
}
