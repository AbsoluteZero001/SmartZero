package com.banksystem.application.HelloController;

import com.alibaba.fastjson.JSONArray;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.utills.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/user/list")
public class GetUser extends HttpServlet {

    private UserInfoDao userInfoDao = new UserInfoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        String token = req.getHeader("token");
//        if (Objects.isNull(ConvertUtils.ADMIN_LOGIN_MAP.get(token))) {
////            resp.sendRedirect("/login");
//            ResponseUtil.fail(resp,ErrorCode.NOT_LOGIN);
//            return;
//        }
        JSONArray userList = userInfoDao.queryByAll();
        ResponseUtil.success(resp, userList);

    }
}
