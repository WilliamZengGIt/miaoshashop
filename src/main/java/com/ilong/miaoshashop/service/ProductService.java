package com.ilong.miaoshashop.service;

import com.ilong.miaoshashop.entity.Product;
import com.ilong.miaoshashop.entity.vo.ProductVo;


import java.util.List;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 16:05
 */

public interface ProductService {

    List<ProductVo> listProductVo();
    ProductVo getProductVoByProductId(long productid);
    void resetStock(List<ProductVo> ProductVoList);
    boolean reduceStock(ProductVo productVo);
    ProductVo getGoodsVoByGoodsId(long productid);
}
