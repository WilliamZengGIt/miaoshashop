package com.ilong.miaoshashop.entity;

import com.ilong.miaoshashop.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-14 19:55
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "t_order_miaosha")
public class OrderMiaosha {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//表Id


    private Long orderId;//订单id

    private Long productId;//商品id





    private Integer userId;//用户id
    private Date created;

    private Date updated;



}
