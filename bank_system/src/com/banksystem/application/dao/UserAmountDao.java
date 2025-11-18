package com.banksystem.application.dao;

import com.banksystem.application.db.Database;
import com.banksystem.application.entity.UserAmount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserAmountDao {

    public Long addOne(UserAmount userAmount) {
        Long reId = 0L;
        String sql = "INSERT INTO user_amount (user_id, amount, balance) VALUES (?, ?, ?)";

        try (Connection conn = Database.conn();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, userAmount.getUserId());
            ps.setLong(2, userAmount.getAmount());
            ps.setLong(3, userAmount.getBalance());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        reId = rs.getLong(1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reId; // 无论是否异常，都会返回结果
    }
}
