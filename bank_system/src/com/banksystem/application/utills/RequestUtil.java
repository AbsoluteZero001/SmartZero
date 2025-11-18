package com.banksystem.application.utills;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.entity.AdminInfo;
import com.banksystem.application.entity.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 请求工具类
 * 用于提取请求参数、Token、登录校验等
 */
public class RequestUtil {

  /**
   * 获取前端请求 JSON 参数
   */
  public static JSONObject getParam(HttpServletRequest request) throws IOException {
    BufferedReader streamReader = new BufferedReader(
            new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
    StringBuilder responseStrBuilder = new StringBuilder();
    String inputStr;
    while ((inputStr = streamReader.readLine()) != null) {
      responseStrBuilder.append(inputStr);
    }
    return JSONObject.parseObject(responseStrBuilder.toString(), JSONObject.class);
  }

  /**
   * 从请求头获取 Token
   */
  public static String getTokenFormRequestHeader(HttpServletRequest request) {
    return request.getHeader("token");
  }

  /**
   * 检查管理员登录状态
   * 从登录信息缓存（LoginInfo.ADMIN_LOGIN_MAP）中取出 AdminInfo
   */
  public static AdminInfo checkAdminLogin(HttpServletRequest request) {
    String token = getTokenFormRequestHeader(request);
    if (token == null || token.isEmpty()) {
      return null;
    }
    return ConvertUtils.ADMIN_LOGIN_MAP.get(token);
  }

  /**
   * 检查普通用户登录状态
   */
  public static UserInfo checkUserLogin(HttpServletRequest request) {
    String token = getTokenFormRequestHeader(request);
    if (token == null || token.isEmpty()) {
      return null;
    }
    return ConvertUtils.USER_LOGIN_MAP.get(token);
  }

  // 在RequestUtil.java文件中添加以下方法
  public static void fail(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    JSONObject result = new JSONObject();
    result.put("code", errorCode.getCode());
    result.put("message", errorCode.getMessage());
    response.getWriter().write(result.toJSONString());
  }

}
