package com.ilong.miaoshashop.entity;

import com.ilong.miaoshashop.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单
 *
 * @author long
 * @date 2020-01-14 19:52
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "t_order")
public class Order  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//表Id


    private String outTradeNo;//商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。

    private Integer productCount;//商品数量

    private String outWxNo;//微信订单号

    private Integer totalFee;//订单总金额，单位为分.


    private String orderStatus;//订单交易状态 4001 交易成功 4002交易失败 4003交易取消


    private Long productId;//商品id

    private  String productName;//商品名

    private Integer productType;//商品类型 0爆款 1普通

    private Double productPrice;//商品价格

    private String payStatus;//支付状态 5001 支付成功  5002 支付失败 5003支付异常


    private Integer userId;//用户id


    private Integer addressId;//选择的地址id


    private Integer physiqueId;//体型id

    private Integer fkCompanyId;//关联的公司id

    private Date created;

    private Date updated;

}
