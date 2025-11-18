package com.banksystem.application.Filter;

import com.banksystem.application.utills.ConvertUtils;
import com.banksystem.application.utills.ErrorCode;
import com.banksystem.application.utills.ResponseUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

/**
 * 登录验证过滤器
 * 用于验证用户是否已登录，拦截未登录用户的请求
 */
@WebFilter("/*")
public class LoginCore implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // 获取请求方法
        String method = req.getMethod();
        System.out.println("method: " + method);

        // 处理 OPTIONS 预检请求，直接放行
        if ("options".equals(method.toLowerCase())) {
            filterChain.doFilter(req, resp);
            return;
        }

        // 获取请求路径
        String url = req.getRequestURI();

        // 登录接口放行
        if ("/login".equals(url)) {
            filterChain.doFilter(req, resp);
            return;
        }

        // 从请求头中获取 token
        String token = req.getHeader("token");

        // 未携带 token
        if (token == null || token.isEmpty()) {
            ResponseUtil.fail(resp, ErrorCode.NOT_LOGIN);
            return;
        }

        // 校验 token 是否存在于管理员或普通用户登录 map 中
        if (Objects.isNull(ConvertUtils.ADMIN_LOGIN_MAP.get(token))
                && Objects.isNull(ConvertUtils.USER_LOGIN_MAP.get(token))) {
            ResponseUtil.fail(resp, ErrorCode.NOT_LOGIN);
            return;
        }

        // 验证通过，放行
        filterChain.doFilter(req, resp);
    }
}
