package com.xupeng.edu.service.impl;

import com.xupeng.edu.exception.UserException;
import com.xupeng.edu.model.Order;
import com.xupeng.edu.model.OrderItem;
import com.xupeng.edu.model.User;
import com.xupeng.edu.service.IOrderService;
import com.xupeng.edu.service.IUserService;
import com.xupeng.edu.service.base.BaseServiceImpl;

import com.xupeng.edu.utils.SendJMail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 10640
 * Date: 2019-05-06
 * Time: 11:31
 */
@Service
@Transactional
public class OrderServiceImpl extends BaseServiceImpl<Order> implements IOrderService {


    @Override
    public void createOder(Order order) {
        try {

            orderMapper.add(order);
            orderItemMapper.addOrderItems(order.getItems());

            for (OrderItem orderItem : order.getItems()) {
                productMapper.updatePNum(orderItem.getProduct().getId(),orderItem.getBuynum());
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public List<Order> findOrdersByUserId(String userid) {
        try {
            return orderMapper.findOrdersByUserId(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Order findOrderByOrderId(String orderId) {
        try {
            Order order = orderMapper.findOrderByOrderId(orderId);
            List<OrderItem> items = new ArrayList<OrderItem>();
            items = orderItemMapper.findOrderItemByOrderId(orderId);
            order.setItems(items);
            return order;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void payByOrderId(String orderId) {
        try {
            orderMapper.payByOrderId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delOrderByOrderId(String orderId) {
        try {

            orderMapper.delOrderByOrderId(orderId);
            orderItemMapper.delOrderItemByOrderId(orderId);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
