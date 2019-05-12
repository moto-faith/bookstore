package com.xupeng.edu.service;

import com.xupeng.edu.model.Order;
import com.xupeng.edu.model.PageResult;
import com.xupeng.edu.model.Product;
import com.xupeng.edu.service.base.IBaseService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 10640
 * Date: 2019-05-06
 * Time: 11:29
 */
public interface IOrderService extends IBaseService<Order> {
    public void createOder(Order order);
    public List<Order> findOrdersByUserId(String userid);
    public Order findOrderByOrderId(String orderId);
    public void payByOrderId(String orderId);
    public void delOrderByOrderId(String orderId);



}
