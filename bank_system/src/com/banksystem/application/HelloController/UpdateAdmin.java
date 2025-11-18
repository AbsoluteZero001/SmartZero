package com.banksystem.application.HelloController;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.AdminInfoDao;
import com.banksystem.application.entity.AdminInfo;
import com.banksystem.application.utills.RequestUtil;
import com.banksystem.application.utills.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/updateAdmin")
/**
 * UpdateAdmin类继承自HttpServlet，用于处理管理员信息更新的请求
 * 该类实现了doPost方法，用于接收和处理POST请求
 */
public class UpdateAdmin extends HttpServlet {

    // 创建AdminInfoDao对象，用于操作管理员信息数据
        public static AdminInfoDao adminInfoDao = new AdminInfoDao();
    /**
     * 重写doPost方法，处理管理员信息更新请求
     * @param req HttpServletRequest对象，包含客户端请求信息
     * @param resp HttpServletResponse对象，用于向客户端返回响应
     * @throws ServletException 可能抛出的Servlet异常
     * @throws IOException 可能抛出的IO异常
     */
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 从请求中获取JSON参数
            JSONObject param= RequestUtil.getParam(req);
        // 获取请求参数中的手机号
            String mobile = param.getString("mobile");
        // 获取请求参数中的密码
            String password = param.getString("pwd");
        // 获取请求参数中的昵称
            String nickname = param.getString("nn");
        // 获取请求参数中的真实姓名
            String name = param.getString("n");
        // 获取请求参数中的管理员ID
            Long id = param.getLong("id");

        // 创建AdminInfo对象并设置属性
            AdminInfo adminInfo = new AdminInfo();
            adminInfo.setId(id);
            adminInfo.setMobile(mobile);
            adminInfo.setNickname(nickname);
            adminInfo.setPassword(password);
            adminInfo.setName(name);
        // 调用adminInfoDao的updateAdmin方法更新管理员信息
            adminInfoDao.updateAdmin(adminInfo);
        // 向客户端返回成功响应
            ResponseUtil.success(resp, null);


        }
    }


