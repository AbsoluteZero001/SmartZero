package com.banksystem.application.HelloController;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.entity.UserInfo;
import com.banksystem.application.utills.ErrorCode;
import com.banksystem.application.utills.RequestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;


@WebServlet("/addUser")
public class AddUserController extends HttpServlet {
    private UserInfoDao userInfoDao = new UserInfoDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject param = RequestUtil.getParam(req);
        String name = param.getString("name");
        String password = param.getString("password");
        String nickname = param.getString("nickname");
        String mobile = param.getString("mobile");

        UserInfo userInfo = userInfoDao.queryByMobile(mobile);

        if (Objects.nonNull(userInfo)) {
            RequestUtil.fail(resp, ErrorCode.USER_MOBILE_SAME);
            return;
        }
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setName(name);
        userInfo1.setPassword(password);
        userInfo1.setNickname(nickname);
        userInfo1.setMobile(mobile);


    }
}
