package com.xupeng.edu.service;

import com.xupeng.edu.exception.UserException;
import com.xupeng.edu.model.Category;
import com.xupeng.edu.model.User;
import com.xupeng.edu.service.base.IBaseService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 10640
 * Date: 2019-05-06
 * Time: 11:29
 */
public interface ICategoryService extends IBaseService<Category> {

    public List<Category> findAll();

}
