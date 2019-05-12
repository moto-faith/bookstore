package com.xupeng.edu.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xupeng.edu.model.Category;
import com.xupeng.edu.model.PageResult;
import com.xupeng.edu.model.Product;
import com.xupeng.edu.service.ICategoryService;
import com.xupeng.edu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 10640
 * Date: 2019-05-06
 * Time: 11:08
 */
@Controller
public class ProductController {

    @Autowired
    private IProductService productService;

    @RequestMapping("showProductByPage")
    public void showProductByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String category = request.getParameter("category");
        String pageStr = request.getParameter("page");
        int page = 1;
        if (pageStr!=null && !"".equals(pageStr)){
            page = Integer.parseInt(pageStr);
        }
        PageResult<Product> pageResult = productService.findBooks(category, page);
        request.setAttribute("pageResult",pageResult);
        request.setAttribute("category",category);
        request.getRequestDispatcher("/product_list.jsp").forward(request,response);
    }

    @RequestMapping("productInfo")
    public void productInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        Product product = productService.findBook(id);
        request.setAttribute("product",product);
        request.getRequestDispatcher("/product_info.jsp").forward(request,response);
    }

    @RequestMapping("findProductBySearch")
    public void findProductBySearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        String pageStr = request.getParameter("page");
        int page = 1;
        if (pageStr!=null && !"".equals(pageStr)){
            page = Integer.parseInt(pageStr);
        }
        PageResult<Product> pageResult = productService.searchBooks(name, page);
        request.setAttribute("pageResult",pageResult);
        request.setAttribute("name",name);
        request.getRequestDispatcher("/search_list.jsp").forward(request,response);
    }




}
