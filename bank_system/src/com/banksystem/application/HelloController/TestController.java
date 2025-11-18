package com.banksystem.application.HelloController;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.AdminInfoDao;
import com.banksystem.application.dao.SystemLogDao;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.entity.AdminInfo;
import com.banksystem.application.entity.UserInfo;
import com.banksystem.application.utills.ConvertUtils;
import com.banksystem.application.utills.ErrorCode;
import com.banksystem.application.utills.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

/**
 * 登录控制器类
 * 处理管理员登录请求
 */
@WebServlet("/login")
public class TestController extends HttpServlet {
    // 管理员数据访问对象
    private AdminInfoDao AdminInfoDao = new AdminInfoDao();
    // 用户数据访问对象
    private UserInfoDao userInfoDao = new UserInfoDao();
    // 系统日志数据访问对象
    private SystemLogDao systemLogDao = new SystemLogDao();

    /**
     * 处理POST请求
     *
     * @param req  HttpServletRequest对象，包含请求信息
     * @param resp HttpServletResponse对象，用于响应客户端
     * @throws ServletException 可能抛出的Servlet异常
     * @throws IOException      可能抛出的IO异常
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 读取请求体内容
        BufferedReader reader = req.getReader();
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        System.out.println(sb.toString());
        // 解析JSON请求
        JSONObject jsonObject = JSONObject.parseObject(sb.toString(), JSONObject.class);
        String mobile = jsonObject.getString("mobile");
        String pwd = jsonObject.getString("pwd");
        String LoginType = jsonObject.getString("loginType");
        // 构建响应JSON
        JSONObject json = new JSONObject();
        json.put("pwd", pwd);
        json.put("mobile", mobile);
        // 处理管理员登录
        if ("admin".equals(LoginType)) {
            // 管理员登录逻辑
            System.out.println("这是管理员端");
            // 根据手机号获取管理员信息
            AdminInfo adminInfo = AdminInfoDao.getAdminByMobile(mobile);
            //1.判断用户是否存在
            if (Objects.isNull(adminInfo)) {
                ResponseUtil.fail(resp, ErrorCode.ADMIN_NOT_EXIST);
                return;
            }
            //2.判断密码是否正确
            if (!adminInfo.getPassword().equals(pwd)) {
                ResponseUtil.fail(resp, ErrorCode.PWD_ERR);
                return;
            }
            System.out.println(adminInfo);
            //3.生成cookie
            String cookieStr = ConvertUtils.getCookie(16);
            ConvertUtils.ADMIN_LOGIN_MAP.put(cookieStr, adminInfo);
            Cookie cookie = new Cookie("token", cookieStr);
            resp.addCookie(cookie);
            json.put("token", cookieStr);
            json.put("name", adminInfo.getName());
            ResponseUtil.success(resp, json);
            jsonObject = new JSONObject();
            jsonObject.put("adminInfo", adminInfo);
            ResponseUtil.success(resp, jsonObject);
        } else if ("user".equals(LoginType)) { // 处理用户登录
            //用户登录逻辑
            System.out.println("这是用户端");
            UserInfoDao userInfoDao = new UserInfoDao();
            UserInfo userInfo = userInfoDao.queryByMobile(mobile);
            if (Objects.isNull(userInfo)) {
                ResponseUtil.fail(resp, ErrorCode.USER_NOT_EXIST);
                return;
            }
            if (!userInfo.getPassword().equals(pwd)) {
                ResponseUtil.fail(resp, ErrorCode.PWD_ERR);
                return;
            }
            String cookieStr = ConvertUtils.getCookie(16);
            ConvertUtils.USER_LOGIN_MAP.put(cookieStr, userInfo);
            Cookie cookie = new Cookie("token", cookieStr);
            resp.addCookie(cookie);
            json.put("token", cookieStr);
            json.put("name", userInfo.getName());
            ResponseUtil.success(resp, json);
        } else {
            ResponseUtil.fail(resp, ErrorCode.LOGIN_TYPE_ERROR);
        }
    }
}

