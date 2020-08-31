package com.ilong.miaoshashop.base;

import java.util.List;

/**
 * @author lin
 * @date 2018/1/29
 */
/*
抽象接口interface
  增删改查方法的实现
*/
public interface BaseDao<E extends BaseEntity> {

  E get(E entity);

  List<E> list(E entity);   //查找方法

  int insert(E entity);        //插入方法

  int update(E entity);        //更新方法

  int delete(E entity);        //删除方法

}
