package com.xupeng.edu.service.impl;

import com.xupeng.edu.exception.UserException;
import com.xupeng.edu.model.User;
import com.xupeng.edu.service.IUserService;
import com.xupeng.edu.service.base.BaseServiceImpl;
import com.xupeng.edu.utils.SendJMail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 10640
 * Date: 2019-05-06
 * Time: 11:31
 */
@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    @Override
    public void modifyUserInfo(User user) throws UserException {
        try {
            userMapper.modifyUserInfo(user);
        } catch (Exception e) {
            throw new UserException("修改用户失败");
        }
    }

    @Override
    public void register(User user) throws UserException {
        try {
            userMapper.addUser(user);

            String link = "http://localhost:8080/active.do?activeCode=" + user.getActiveCode();

            String html = "<a href=\"" + link + "\">欢迎你注册网上书城帐号,请点击激活</a>";
            System.out.println(html);
            //发送激活邮件
            SendJMail.sendMail(user.getEmail(), html);

        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("注册失败，用户名重复");
        }
    }

    @Override
    public void activeUser(String activeCode) throws UserException {
        try {
            User user = userMapper.findUserByActiveCode(activeCode);
            if (user==null){
                throw new UserException("非法激活，用户不存在");
            }
            if (user!=null && user.getState()==1){
                throw new UserException("用户已经激活过了...");
            }
            userMapper.updateState(activeCode);
        } catch (Exception e) {
            throw new UserException("激活失败");
        }
    }

    @Override
    public User login(String username, String password) throws UserException {

        User user = userMapper.findUserByUsernameAndPassword(username,password);
        if (user==null){
            throw new UserException("用户名或者密码不正确");
        }
        if (user!=null && user.getState()==0){
            throw new UserException("用户未激活，请先登录邮件进行激活");
        }
        return user;

    }

    @Override
    public User findUserById(String id) throws UserException {
        try {
            User user = userMapper.findUserById(id);
            if (user==null){
                throw new UserException("用户名不存在");
            }
            return user;
        } catch (Exception e) {
            throw new UserException("查找用户失败");
        }
    }

}
