package com.xupeng.edu.service.impl;

import com.xupeng.edu.model.Category;
import com.xupeng.edu.model.PageResult;
import com.xupeng.edu.model.Product;
import com.xupeng.edu.service.ICategoryService;
import com.xupeng.edu.service.IProductService;
import com.xupeng.edu.service.base.BaseServiceImpl;
import com.xupeng.edu.utils.JedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 10640
 * Date: 2019-05-06
 * Time: 11:31
 */
@Service
@Transactional
public class ProductServiceImpl extends BaseServiceImpl<Product> implements IProductService {


    @Override
    public PageResult<Product> findBooks(String category, int page) {
        try {
            PageResult<Product> pr= new PageResult<Product>();
            long totalCount = productMapper.count(category);
            pr.setTotalCount(totalCount);
            int totalPage = (int) Math.ceil(totalCount*1.0/pr.getPageSize());
            pr.setTotalPage(totalPage);
            pr.setCurrentPage(page);
            int start = (page-1)*pr.getPageSize();
            List<Product> list = productMapper.findBooks(category, start, pr.getPageSize());
            pr.setList(list);
            return pr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product findBook(String id) {
        try {
            return productMapper.findBook(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PageResult<Product> searchBooks(String name, int page) {
        try {
            PageResult<Product> pr= new PageResult<Product>();
            long totalCount = productMapper.searchCount(name);
            pr.setTotalCount(totalCount);
            int totalPage = (int) Math.ceil(totalCount*1.0/pr.getPageSize());
            pr.setTotalPage(totalPage);
            pr.setCurrentPage(page);
            int start = (page-1)*pr.getPageSize();
            List<Product> list = productMapper.searchBooks(name, start, pr.getPageSize());
            pr.setList(list);
            return pr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
