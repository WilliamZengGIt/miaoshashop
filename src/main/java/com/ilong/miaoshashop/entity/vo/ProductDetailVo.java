package com.ilong.miaoshashop.entity.vo;

import com.ilong.miaoshashop.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 18:29
 */
@Data
@NoArgsConstructor
public class ProductDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private ProductVo productVo;
    private User user;

}
