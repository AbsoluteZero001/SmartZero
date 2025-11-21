package com.banksystem.application.HelloController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.AdminInfoDao;
import com.banksystem.application.dao.TransactionDao;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.entity.AdminInfo;
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

@WebServlet("/admin")
public class AdminController extends HttpServlet {
    private AdminInfoDao adminInfoDao = new AdminInfoDao();
    private UserInfoDao userInfoDao = new UserInfoDao();
    private TransactionDao transactionDao = new TransactionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if admin is logged in
        AdminInfo currentAdmin = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    currentAdmin = ConvertUtils.ADMIN_LOGIN_MAP.get(token);
                    break;
                }
            }
        }

        if (currentAdmin == null) {
            ResponseUtil.fail(resp, 401, "Admin not logged in");
            return;
        }

        // Get action parameter
        String action = req.getParameter("action");
        
        if ("users".equals(action)) {
            // Return all users
            JSONArray users = userInfoDao.queryByAll();
            JSONObject result = new JSONObject();
            result.put("users", users);
            ResponseUtil.success(resp, result);
        } else if ("transactions".equals(action)) {
            // Return all transactions
            JSONArray transactions = transactionDao.getAllTransactions();
            JSONObject result = new JSONObject();
            result.put("transactions", transactions);
            ResponseUtil.success(resp, result);
        } else {
            ResponseUtil.fail(resp, 400, "Invalid action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if admin is logged in
        AdminInfo currentAdmin = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    currentAdmin = ConvertUtils.ADMIN_LOGIN_MAP.get(token);
                    break;
                }
            }
        }

        if (currentAdmin == null) {
            ResponseUtil.fail(resp, 401, "Admin not logged in");
            return;
        }

        // Read request body
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // Parse JSON request
        JSONObject jsonObject = JSONObject.parseObject(sb.toString());
        String action = jsonObject.getString("action");

        if ("addUser".equals(action)) {
            // Add new user functionality would go here
            ResponseUtil.success(resp, "User added successfully");
        } else if ("disableUser".equals(action)) {
            // Disable user functionality would go here
            ResponseUtil.success(resp, "User disabled successfully");
        } else {
            ResponseUtil.fail(resp, 400, "Invalid action");
        }
    }
}