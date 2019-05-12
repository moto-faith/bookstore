package com.xupeng.edu.web.controller;

import com.xupeng.edu.exception.UserException;
import com.xupeng.edu.model.User;
import com.xupeng.edu.service.IUserService;
import com.xupeng.edu.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 10640
 * Date: 2019-05-06
 * Time: 11:08
 */
@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("register")
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        try {
            String checkcode_client = request.getParameter("checkcode");
            String checkcode_session = (String) request.getSession().getAttribute("checkcode_session");
            if (!checkcode_client.equals(checkcode_session)){
                request.setAttribute("checkcode_err","验证错误");
                request.getRequestDispatcher("/register.jsp").forward(request,response);
                return;
            }

            BeanUtils.populate(user,request.getParameterMap());
            user.setActiveCode(UUID.randomUUID().toString());//激活码
            user.setRole("普通用户");//角色
            user.setRegistTime(new Date());
            System.out.println(user);
            userService.register(user);
            request.getRequestDispatcher("/registersuccess.jsp").forward(request, response);

        }catch (UserException e) {
            e.printStackTrace();
            request.setAttribute("register_err", e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            User user = userService.login(username, password);
            System.out.println(user);
            request.getSession().setAttribute("user",user);
            if ("管理员".equals(user.getRole())){
                response.sendRedirect(request.getContextPath()+"/admin/login/home.jsp");
            }else {
                response.sendRedirect(request.getContextPath()+"/index.jsp");
            }
        } catch (UserException e) {
            request.setAttribute("login_msg",e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
    @RequestMapping("myaccount")
    public void myaccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            response.sendRedirect(request.getContextPath()+"/login.jsp");
        }else {
            response.sendRedirect(request.getContextPath()+"/myAccount.jsp");
        }
    }

    @RequestMapping("active")
    public void active(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("content-type","text/html;charset=utf-8");
        String activeCode = request.getParameter("activeCode");
        try {
            userService.activeUser(activeCode);
            request.getRequestDispatcher("/activesuccess.jsp").forward(request, response);
        } catch (UserException e) {
            response.getWriter().write(e.getMessage());
        }
    }

    @RequestMapping("modifyUserInfo")
    public void modifyUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = new User();
        try {
            BeanUtils.populate(user,request.getParameterMap());
            userService.modifyUserInfo(user);
            response.sendRedirect(request.getContextPath()+"/modifyUserInfoSuccess.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write(e.getMessage());
        }
    }

    @RequestMapping("findUserById")
    public void findUserById(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        try {
            User user = userService.findUserById(id);
            request.setAttribute("u",user);
            request.getRequestDispatcher("/modifyuserinfo.jsp").forward(request,response);
        } catch (UserException e) {
            response.getWriter().write(e.getMessage());
        }
    }

    @RequestMapping("logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath()+"/index.jsp");
    }


}
