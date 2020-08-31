package com.ilong.miaoshashop.dao;

import com.ilong.miaoshashop.entity.OrderMiaosha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 20:33
 */
public interface OrderMiaoshaDao extends JpaRepository<OrderMiaosha,Long> {
    @Query(value = "delete from t_order_miaosha ", nativeQuery = true)
    @Modifying
    public void  deleteOrderMiaosha();
}
