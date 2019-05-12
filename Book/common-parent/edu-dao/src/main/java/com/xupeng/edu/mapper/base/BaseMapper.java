package com.xupeng.edu.mapper.base;

import com.xupeng.edu.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 10640
 * Date: 2019-05-06
 * Time: 11:18
 */
public interface BaseMapper<T> {

    public List<T> findAll();


    public void add(T order);
    public List<T> findOrdersByUserId(@Param("id")String id);
    public T findOrderByOrderId(@Param("orderId")String orderId);
    public void payByOrderId(@Param("orderId")String orderId);
    public void delOrderByOrderId(@Param("orderId")String orderId);


    public void addOrderItems(List<T> items);
    public void delOrderItemByOrderId(@Param("orderId")String orderId);
    public List<T> findOrderItemByOrderId(@Param("orderId")String orderId);


    public long count(@Param("category")String category);
    public long searchCount(@Param("name")String name);
    public List<T> findBooks(@Param("category")String category, @Param("page")int page, @Param("pageSize")int pageSize);
    public T findBook(@Param("id")String id);
    public void updatePNum(@Param("id")int id, @Param("buynum")int buynum);
    public List<T> searchBooks(@Param("name")String name, @Param("page")int page, @Param("pageSize")int pageSize);

    public void addUser(T user);
    public T findUserByActiveCode(String activeCode);
    public void updateState(String activeCode);
    public void modifyUserInfo(T user);
    public T findUserByUsernameAndPassword(String username,String password);
    public T findUserById(String id);

}
