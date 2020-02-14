package com.java.web.servlet;

import com.java.dao.UserDao;
import com.java.entity.User;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static org.apache.commons.beanutils.BeanUtils.*;

@WebServlet("/loginServlet")//servlet资源名称
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.设置编码
        req.setCharacterEncoding("utf-8");
       /* //2.获取请求参数
        String username=req.getParameter("username");
        String password=req.getParameter("password");
        //3.封装user对象
        User loginUser=new User();
        loginUser.setUsername(username);
        loginUser.setPassword(password);*/

        //2.获取request所有对请求参数
        Map<String, String[]> parameterMap = req.getParameterMap();
        //3.创建user对象
        User loginUser = new User();
        //3.2使用beanUtils封装
        try {
            BeanUtils.populate(loginUser, parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //4.调用UserDao中的login方法
        UserDao userDao = new UserDao();
        User user = userDao.login(loginUser);
        //5.判断user
        if (user == null) {
            //登录失败，请求转发到失败资源路径
            req.getRequestDispatcher("/failServlet").forward(req, resp);
            System.out.println("fail");

        } else {
            System.out.println("success");
            //登录成功
            //存储数据
            req.setAttribute("user", user);
            //转发
            req.getRequestDispatcher("/successServlet").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
