package com.xupeng.edu.service;

import com.xupeng.edu.exception.UserException;
import com.xupeng.edu.model.User;
import com.xupeng.edu.service.base.IBaseService;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 10640
 * Date: 2019-05-06
 * Time: 11:29
 */
public interface IUserService extends IBaseService<User> {

    public void register(User user) throws UserException;
    public void activeUser(String activeCode) throws UserException;
    public User login(String username,String password) throws UserException;
    public User findUserById(String id) throws UserException;
    public void modifyUserInfo(User user) throws UserException;

}
