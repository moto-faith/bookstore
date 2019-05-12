package com.xupeng.edu.service;

import com.xupeng.edu.model.Category;
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
public interface IProductService extends IBaseService<Product> {

    public PageResult<Product> findBooks(String category, int page);
    public Product findBook(String id);
    public PageResult<Product> searchBooks(String name, int page);


}
