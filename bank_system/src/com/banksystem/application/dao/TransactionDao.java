package com.banksystem.application.dao;

import com.alibaba.fastjson.JSONArray;
import com.banksystem.application.db.Database;
import com.banksystem.application.entity.Transaction;
import com.banksystem.application.utills.ConvertUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    /**
     * Add a new transaction record
     *
     * @param transaction Transaction entity to add
     * @return Generated ID of the new transaction
     */
    public Long addOne(Transaction transaction) {
        Long id = 0L;
        String sql = "INSERT INTO transaction (user_id, transaction_type, amount, balance_after, description, create_by, update_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConn();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, transaction.getUserId());
            ps.setString(2, transaction.getTransactionType());
            ps.setLong(3, transaction.getAmount());
            ps.setLong(4, transaction.getBalanceAfter());
            ps.setString(5, transaction.getDescription());
            ps.setLong(6, transaction.getCreateBy());
            ps.setLong(7, transaction.getUpdateBy());

            int result = ps.executeUpdate();
            if (result > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    /**
     * Get transaction history for a specific user
     *
     * @param userId User ID to get transactions for
     * @return List of transactions
     */
    public JSONArray getTransactionHistory(Long userId) {
        JSONArray transactions = new JSONArray();
        String sql = "SELECT id, user_id, transaction_type, amount, balance_after, description, " +
                "is_deleted, create_by, update_by, create_time, update_time " +
                "FROM transaction WHERE user_id = ? AND is_deleted = FALSE ORDER BY create_time DESC";

        try (Connection conn = Database.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getLong("id"));
                transaction.setUserId(rs.getLong("user_id"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setAmount(rs.getLong("amount"));
                transaction.setBalanceAfter(rs.getLong("balance_after"));
                transaction.setDescription(rs.getString("description"));
                transaction.setDeleted(rs.getBoolean("is_deleted"));
                transaction.setCreateBy(rs.getLong("create_by"));
                transaction.setUpdateBy(rs.getLong("update_by"));
                transaction.setCreateTime(ConvertUtils.toInstant(rs.getString("create_time")));
                transaction.setUpdateTime(ConvertUtils.toInstant(rs.getString("update_time")));
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    /**
     * Get all transactions (for admin)
     *
     * @return List of all transactions
     */
    public JSONArray getAllTransactions() {
        JSONArray transactions = new JSONArray();
        String sql = "SELECT id, user_id, transaction_type, amount, balance_after, description, " +
                "is_deleted, create_by, update_by, create_time, update_time " +
                "FROM transaction WHERE is_deleted = FALSE ORDER BY create_time DESC";

        try (Connection conn = Database.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getLong("id"));
                transaction.setUserId(rs.getLong("user_id"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setAmount(rs.getLong("amount"));
                transaction.setBalanceAfter(rs.getLong("balance_after"));
                transaction.setDescription(rs.getString("description"));
                transaction.setDeleted(rs.getBoolean("is_deleted"));
                transaction.setCreateBy(rs.getLong("create_by"));
                transaction.setUpdateBy(rs.getLong("update_by"));
                transaction.setCreateTime(ConvertUtils.toInstant(rs.getString("create_time")));
                transaction.setUpdateTime(ConvertUtils.toInstant(rs.getString("update_time")));
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
}