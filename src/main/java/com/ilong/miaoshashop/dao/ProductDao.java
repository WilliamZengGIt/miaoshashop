package com.ilong.miaoshashop.dao;

import com.ilong.miaoshashop.entity.vo.ProductVo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 22:33
 */
public interface  ProductDao extends JpaRepository<ProductVo ,Long> {
    @Modifying
    @Query("update ProductVo m set m.productStock = ?2 where m.id = ?1")
    public int resetStock(long id,int productStock);

    @Modifying
    @Query("update ProductVo m set m.productStock=?1 where  m.id=?2 and productStock > 0")
    public int reduceStock(int productStock, long id);
}
