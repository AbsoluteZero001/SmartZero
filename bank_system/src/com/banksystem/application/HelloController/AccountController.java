package com.banksystem.application.HelloController;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.entity.UserInfo;
import com.banksystem.application.utills.ConvertUtils;
import com.banksystem.application.utills.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/account")
public class AccountController extends HttpServlet {
    private UserInfoDao userInfoDao = new UserInfoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get user from session/cookie
        UserInfo currentUser = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    currentUser = ConvertUtils.USER_LOGIN_MAP.get(token);
                    break;
                }
            }
        }

        if (currentUser == null) {
            ResponseUtil.fail(resp, 401, "User not logged in");
            return;
        }

        // Return user account information
        JSONObject result = new JSONObject();
        result.put("id", currentUser.getId());
        result.put("cardType", currentUser.getCardType());
        result.put("cardNo", currentUser.getCardNo());
        result.put("nickname", currentUser.getNickname());
        result.put("name", currentUser.getName());
        result.put("address", currentUser.getAddress());
        result.put("idNum", currentUser.getIdNum());
        result.put("mobile", currentUser.getMobile());
        result.put("state", currentUser.getState());
        
        ResponseUtil.success(resp, result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Read request body
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // Parse JSON request
        JSONObject jsonObject = JSONObject.parseObject(sb.toString());
        
        // Get user from session/cookie
        UserInfo currentUser = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    currentUser = ConvertUtils.USER_LOGIN_MAP.get(token);
                    break;
                }
            }
        }

        if (currentUser == null) {
            ResponseUtil.fail(resp, 401, "User not logged in");
            return;
        }

        // Update user information
        currentUser.setPassword(jsonObject.getString("password"));
        currentUser.setNickname(jsonObject.getString("nickname"));
        currentUser.setName(jsonObject.getString("name"));
        currentUser.setMobile(jsonObject.getString("mobile"));
        currentUser.setAddress(jsonObject.getString("address"));
        
        // Update in database
        userInfoDao.updateUser(currentUser);
        
        JSONObject result = new JSONObject();
        result.put("message", "Account updated successfully");
        ResponseUtil.success(resp, result);
    }
}