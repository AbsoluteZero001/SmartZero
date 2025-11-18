package com.banksystem.application.HelloController;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.AdminInfoDao;
import com.banksystem.application.entity.AdminInfo;
import com.banksystem.application.utills.ErrorCode;
import com.banksystem.application.utills.RequestUtil;
import com.banksystem.application.utills.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/addAdmin")
public class AddAdminController extends HttpServlet {

    private final AdminInfoDao adminInfoDao = new AdminInfoDao();

    /**
     * 处理HTTP POST请求：添加管理员信息
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        JSONObject parm = RequestUtil.getParam(req);
        String name = parm.getString("name");
        String password = parm.getString("password");
        String nickName = parm.getString("nickName");
        String phone = parm.getString("phone");

        // 参数校验
        if (name == null || password == null || phone == null) {
            ResponseUtil.fail(resp, ErrorCode.ADMIN_MOBILE_SAME);
            return;
        }

        // 检查手机号是否重复
        AdminInfo adminInfo = adminInfoDao.getAdminByMobile(phone);
        if (Objects.nonNull(adminInfo)) {
            ResponseUtil.fail(resp, ErrorCode.ADMIN_MOBILE_SAME);
            return;
        }

        // 创建新管理员对象
        adminInfo = new AdminInfo();
        adminInfo.setName(name);
        adminInfo.setNickname(nickName);
        adminInfo.setMobile(phone);
        adminInfo.setPassword(password);

        // 保存到数据库
        adminInfoDao.addAdmin(adminInfo);

        // 返回成功响应
        ResponseUtil.success(resp, null);
    }
}
