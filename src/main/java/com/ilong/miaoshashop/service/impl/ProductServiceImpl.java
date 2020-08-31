package com.ilong.miaoshashop.service.impl;

import com.ilong.miaoshashop.dao.ProductDao;
import com.ilong.miaoshashop.entity.Product;
import com.ilong.miaoshashop.entity.vo.ProductVo;
import com.ilong.miaoshashop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-16 16:31
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;


    @Override
    public boolean reduceStock(ProductVo productVo) {


        int ret = productDao.reduceStock(productVo.getProductStock()-1,productVo.getId());
        return ret > 0;

       
    }

    @Override
    public ProductVo getProductVoByProductId(long productid) {
        ProductVo productVo = productDao.getOne(productid);
        return productVo;
    }

    @Override
    public void resetStock(List<ProductVo> ProductVoList) {
        for(ProductVo goods : ProductVoList ) {
            productDao.resetStock(goods.getId(),goods.getProductStock());
        }
    }

    @Override
    public List<ProductVo> listProductVo() {

        return  productDao.findAll();
    }

    @Override
    public ProductVo getGoodsVoByGoodsId(long productid) {
        return productDao.findOne(productid);
    }
}
