package com.ilong.miaoshashop.entity;

import com.ilong.miaoshashop.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-14 19:42
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "t_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;//表Id


    private String productName;//商品名
    private String productImgLink;//商品图片链接

    private String productInfo;//商品详情

    private Double productPrice;//商品价格

    public Integer productStock;//商品库存

    private Integer productMenuId;//产品归属类型id


    private Integer fkCompanyId;//企业id


    private Date startDate;
    private Date endDate;

    private Date updated;//更新时间


    private Date created;//创建时间

}
