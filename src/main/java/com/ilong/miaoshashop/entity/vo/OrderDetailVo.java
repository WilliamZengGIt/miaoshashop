package com.ilong.miaoshashop.entity.vo;

import com.ilong.miaoshashop.entity.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 20:10
 */
@Data
@NoArgsConstructor
public class OrderDetailVo {
    private ProductVo productVo;
    private Order order;

}
