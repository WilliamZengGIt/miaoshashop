package com.ilong.miaoshashop.dao;

import com.ilong.miaoshashop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 20:13
 */
public interface OrderDao extends JpaRepository<Order,Long> {

    @Query(value = "delete from t_order", nativeQuery = true)
    @Modifying
    public  void  deleteOrder();
}
