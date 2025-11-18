package com.banksystem.application.dao;

import com.banksystem.application.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 系统日志数据访问对象
 * 用于记录系统中各种操作日志，包括管理员和用户的登录、退出等操作
 */
public class SystemLogDao {
    /**
     * 添加一条系统日志记录的私有方法
     *
     * @param type     日志类型（0表示退出，1表示登录）
     * @param userType 用户类型（0表示管理员，1表示普通用户）
     * @param id       用户ID
     */
    //公用类
    private void addOne(int type, int userType, Long id) {
        Connection conn = Database.getConn(); // 获取数据库连接
        String sql = "INSERT INTO system_log (type, user_type, user_id) VALUES (?, ?, ?)"; // SQL插入语句
        try {
            PreparedStatement ps = conn.prepareStatement(sql); // 创建预处理语句
            ps.setInt(1, type); // 设置第一个参数为日志类型
            ps.setInt(2, userType); // 设置第二个参数为用户类型
            ps.setLong(3, id); // 设置第三个参数为用户ID
            int i = ps.executeUpdate(); // 执行更新操作
            System.out.println("新增成功"); // 输出成功信息
        } catch (Exception e) {
            throw new RuntimeException(e); // 抛出运行时异常
        }
    }

    /**
     * 记录管理员登录日志
     *
     * @param id 管理员ID
     */
    //管理员登录
    public void addLoginAdmin(Long id) {
        addOne(1, 0, id); // 调用私有方法，添加管理员登录记录
    }

    /**
     * 记录管理员退出日志
     *
     * @param id 管理员ID
     */
    //管理员退出
    public void addLogoutAdmin(Long id) {
        addOne(0, 0, id); // 调用私有方法，添加管理员退出记录
    }

    /**
     * 记录用户登录日志
     *
     * @param id 用户ID
     */
    //用户登录
    public void addLoginUser(Long id) {
        addOne(1, 1, id); // 调用私有方法，添加用户登录记录
    }

    /**
     * 记录用户退出日志
     *
     * @param id 用户ID
     */
    //用户退出
    public void addLogoutUser(Long id) {
        addOne(0, 1, id); // 调用私有方法，添加用户退出记录

    }
}
