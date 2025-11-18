package com.banksystem.application.dao;

import com.alibaba.fastjson.JSONArray;
import com.banksystem.application.db.Database;
import com.banksystem.application.entity.UserInfo;
import com.banksystem.application.utills.ConvertUtils;

import java.sql.*;

public class UserInfoDao {
    public Long addOne(UserInfo userInfo) {
        Long id = 0L;
        Connection conn = Database.getConn();
        String sql = "insert into user_info ( card_type, card_no, password, nickname, name, " +
                "address, id_num, mobile) value (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userInfo.getCardType());
            ps.setString(2, userInfo.getCardNo());
            ps.setString(3, userInfo.getPassword());
            ps.setString(4, userInfo.getNickname());
            ps.setString(5, userInfo.getName());
            ps.setString(6, userInfo.getAddress());
            ps.setString(7, userInfo.getIdNum());
            ps.setString(8, userInfo.getMobile());
            int i = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (i > 0) {
                System.out.println("添加成功");
                if (rs.next()) {
                    id = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public UserInfo queryByMobile(String mobile) {
        // 获取连接池
        Connection conn = Database.getConn();
        String sql = "select id, card_type, card_no, password, nickname, name, address, " +
                "id_num, mobile, state, is_deleted, create_by, update_by," +
                " create_time, update_time from user_info where " +
                "state = 1 and mobile = ?";
        UserInfo adminInfo = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, mobile);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                adminInfo = new UserInfo();
                adminInfo.setId(rs.getLong("id"));
                adminInfo.setPassword(rs.getString("password"));
                adminInfo.setMobile(rs.getString("mobile"));
                adminInfo.setCardType(rs.getString("card_type"));
                adminInfo.setCardNo(rs.getString("card_no"));
                adminInfo.setName(rs.getString("name"));
                adminInfo.setIdNum(rs.getString("id_num"));
                adminInfo.setNickname(rs.getString("nickname"));
                adminInfo.setCreateBy(rs.getLong("create_by"));
                adminInfo.setUpdateBy(rs.getLong("update_by"));
                adminInfo.setState(rs.getInt("state"));
                adminInfo.setAddress(rs.getString("address"));
                adminInfo.setDeleted(rs.getBoolean("is_deleted"));
                adminInfo.setUpdateTime(ConvertUtils.toInstant(rs.getString("update_time")));
                adminInfo.setCreateTime(ConvertUtils.toInstant(rs.getString("create_time")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return adminInfo;
    }

    // 2.查询全部
    public JSONArray queryByAll() {
        JSONArray list = new JSONArray();
        // 获取连接池
        Connection conn = Database.getConn();
        String sql = "select id, card_type, card_no, password, nickname, name, address, " +
                "id_num, mobile, state, is_deleted, create_by, update_by," +
                " create_time, update_time from user_info where " +
                "state = 1";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setId(rs.getLong("id"));
                userInfo.setCardType(rs.getString("card_type"));
                userInfo.setCardNo(rs.getString("card_no"));
                userInfo.setPassword(rs.getString("password"));
                userInfo.setNickname(rs.getString("nickname"));
                userInfo.setName(rs.getString("name"));
                userInfo.setAddress(rs.getString("address"));
                userInfo.setIdNum(rs.getString("id_num"));
                userInfo.setMobile(rs.getString("mobile"));
                userInfo.setState(rs.getInt("state"));
                userInfo.setDeleted(rs.getBoolean("is_deleted"));
                userInfo.setCreateBy(rs.getLong("create_by"));
                userInfo.setUpdateBy(rs.getLong("update_by"));
                userInfo.setCreateTime(ConvertUtils.toInstant(rs.getString("create_time")));
                userInfo.setUpdateTime(ConvertUtils.toInstant(rs.getString("update_time")));
                list.add(userInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //3.修改用户信息
    public void updateUser(UserInfo userInfo) {
        //获取连接池
        Connection conn = Database.getConn();
        String sql = "update user_info set password = ?, nickname = ?, name = ?, mobile = ? where id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userInfo.getPassword());
            ps.setString(2, userInfo.getNickname());
            ps.setString(3, userInfo.getName());
            ps.setString(4, userInfo.getMobile());
            ps.setLong(5, userInfo.getId());
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("修改成功");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
