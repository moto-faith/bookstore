package com.xupeng.edu.web.controller;

import com.xupeng.edu.model.*;
import com.xupeng.edu.service.IOrderService;
import com.xupeng.edu.service.IProductService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 10640
 * Date: 2019-05-06
 * Time: 11:08
 */
@Controller
public class OrderController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IOrderService orderService;


    @RequestMapping("addCart")
    public void addCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        Product product = productService.findBook(id);
        Map<Product,Integer> cart = (Map<Product, Integer>) request.getSession().getAttribute("cart");
        if (cart==null){
            cart = new HashMap<Product,Integer>();
            cart.put(product,1);
        }else {
            if (cart.containsKey(product)){
                cart.put(product,cart.get(product)+1);
            }else {
                cart.put(product,1);
            }
        }


//        for(Map.Entry<Product, Integer> entry : cart.entrySet()){
//            System.out.println(entry.getKey() + "数量" + entry.getValue());
//        }
//        System.out.println("===============================");

        request.getSession().setAttribute("cart",cart);
        String a1 = "<a href=\"" + request.getContextPath()+"/showProductByPage.do\">继续购物</a>";
        String a2 = "&nbsp;&nbsp;<a href=\"" + request.getContextPath()+"/cart.jsp\">查看购物车</a>";
        response.setHeader("Content-Type","text/html;charset=utf-8");
        response.getWriter().write(a1);
        response.getWriter().write(a2);
    }

    @RequestMapping("createOrder")
    public void createOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            response.getWriter().write("非法访问...");
            return;
        }

        Map<Product,Integer> cart = (Map<Product, Integer>) request.getSession().getAttribute("cart");
        if (cart==null || cart.size()==0){
            response.getWriter().write("购物车没商品");
            return;
        }

        Order order = new Order();
        try {
            BeanUtils.populate(order,request.getParameterMap());
            order.setId(UUID.randomUUID().toString());
            order.setOrdertime(new Date());
            order.setUser(user);

            List<OrderItem> orderItems = new ArrayList<>();
            double totalPrice = 0;
            for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setBuynum(entry.getValue());
                orderItem.setProduct(entry.getKey());
                orderItem.setOrder(order);

                totalPrice += entry.getKey().getPrice() * entry.getValue();

                orderItems.add(orderItem);

            }

            order.setItems(orderItems);
            order.setMoney(totalPrice);
            orderService.createOder(order);
            request.getSession().removeAttribute("cart");

            response.sendRedirect(request.getContextPath()+"/createOrderSuccess.jsp");


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @RequestMapping("settleAccount")
    public void settleAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }else {
            response.sendRedirect(request.getContextPath() + "/order.jsp");
        }
    }

    @RequestMapping("changeNum")
    public void changeNum(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String num = request.getParameter("num");
        Product product = productService.findBook(id);
        Map<Product,Integer> cart = (Map<Product, Integer>) request.getSession().getAttribute("cart");
        if (cart.containsKey(product)){
            if ("0".equals(num)){
                cart.remove(product);
            }else if (product.getPnum()<Integer.parseInt(num)){
                cart.put(product,product.getPnum());
            }else {
                cart.put(product,Integer.parseInt(num));
            }
        }

        request.getSession().setAttribute("cart",cart);
        response.sendRedirect(request.getContextPath()+"/cart.jsp");
    }

    @RequestMapping("findOrderById")
    public void findOrderById(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            response.getWriter().write("非法访问..");
            return;
        }

        List<Order> orders = orderService.findOrdersByUserId(user.getId() + "");
        request.setAttribute("ordersCount",orders.size());
        request.setAttribute("orders",orders);
        request.getRequestDispatcher("/orderlist.jsp").forward(request,response);
    }

    @RequestMapping("findOrderByOrderId")
    public void findOrderByOrderId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderId = request.getParameter("orderId");
        Order order = orderService.findOrderByOrderId(orderId);
        System.out.println(order);
        request.getSession().setAttribute("order",order);
        request.getRequestDispatcher("/orderInfo.jsp").forward(request,response);
    }

    @RequestMapping("delOrderByOrderId")
    public void delOrderByOrderId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderId = request.getParameter("orderId");
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            response.getWriter().write("您没有权限删除订单");
            return;
        }

        orderService.delOrderByOrderId(orderId);

        request.getRequestDispatcher("/delOrderSuccess.jsp").forward(request,response);
    }

    @RequestMapping("pay")
    public void pay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Order order = (Order) request.getSession().getAttribute("order");
        if (order==null){
            response.getWriter().write("没获取到订单");
            return;
        }
        if (order.getPaystate()==1){
            response.getWriter().write("该订单已支付");
            return;
        }
        orderService.payByOrderId(order.getId());
        response.sendRedirect(request.getContextPath()+"/paysuccess.jsp");
    }




}
