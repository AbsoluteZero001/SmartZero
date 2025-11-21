package com.banksystem.application.HelloController;

import com.alibaba.fastjson.JSONObject;
import com.banksystem.application.dao.TransactionDao;
import com.banksystem.application.dao.UserAmountDao;
import com.banksystem.application.dao.UserInfoDao;
import com.banksystem.application.entity.Transaction;
import com.banksystem.application.entity.UserAmount;
import com.banksystem.application.entity.UserInfo;
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
import java.time.Instant;
import java.util.Objects;

@WebServlet("/transaction")
public class TransactionController extends HttpServlet {
    private UserAmountDao userAmountDao = new UserAmountDao();
    private TransactionDao transactionDao = new TransactionDao();
    private UserInfoDao userInfoDao = new UserInfoDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Read request body
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // Parse JSON request
        JSONObject jsonObject = JSONObject.parseObject(sb.toString());
        String action = jsonObject.getString("action"); // deposit, withdraw, transfer
        Long amount = jsonObject.getLong("amount");

        // Get user from session/cookie
        UserInfo currentUser = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    currentUser = ConvertUtils.USER_LOGIN_MAP.get(token);
                    break;
                }
            }
        }

        if (currentUser == null) {
            ResponseUtil.fail(resp, 401, "User not logged in");
            return;
        }

        try {
            switch (action) {
                case "deposit":
                    handleDeposit(resp, currentUser, amount);
                    break;
                case "withdraw":
                    handleWithdraw(resp, currentUser, amount);
                    break;
                case "transfer":
                    String targetCardNo = jsonObject.getString("targetCardNo");
                    handleTransfer(resp, currentUser, amount, targetCardNo);
                    break;
                default:
                    ResponseUtil.fail(resp, 400, "Invalid action");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.fail(resp, 500, "Transaction failed: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get user from session/cookie
        UserInfo currentUser = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    currentUser = ConvertUtils.USER_LOGIN_MAP.get(token);
                    break;
                }
            }
        }

        if (currentUser == null) {
            ResponseUtil.fail(resp, 401, "User not logged in");
            return;
        }

        // Return transaction history
        JSONObject result = new JSONObject();
        result.put("transactions", transactionDao.getTransactionHistory(currentUser.getId()));
        ResponseUtil.success(resp, result);
    }

    private void handleDeposit(HttpServletResponse resp, UserInfo user, Long amount) throws IOException {
        // Get current balance
        // Note: In a real system, this would be retrieved from the database
        // For simplicity, we'll simulate this functionality
        
        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setUserId(user.getId());
        transaction.setTransactionType("deposit");
        transaction.setAmount(amount);
        // In a real implementation, we would calculate the actual balance
        transaction.setBalanceAfter(amount); 
        transaction.setDescription("Deposit funds");
        transaction.setCreateBy(user.getId());
        transaction.setUpdateBy(user.getId());
        transaction.setCreateTime(Instant.now());
        transaction.setUpdateTime(Instant.now());
        
        // Save transaction
        Long transactionId = transactionDao.addOne(transaction);
        
        if (transactionId > 0) {
            JSONObject result = new JSONObject();
            result.put("message", "Deposit successful");
            result.put("transactionId", transactionId);
            ResponseUtil.success(resp, result);
        } else {
            ResponseUtil.fail(resp, 500, "Failed to record transaction");
        }
    }

    private void handleWithdraw(HttpServletResponse resp, UserInfo user, Long amount) throws IOException {
        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setUserId(user.getId());
        transaction.setTransactionType("withdrawal");
        transaction.setAmount(amount);
        // In a real implementation, we would calculate the actual balance
        transaction.setBalanceAfter(-amount); 
        transaction.setDescription("Withdraw funds");
        transaction.setCreateBy(user.getId());
        transaction.setUpdateBy(user.getId());
        transaction.setCreateTime(Instant.now());
        transaction.setUpdateTime(Instant.now());
        
        // Save transaction
        Long transactionId = transactionDao.addOne(transaction);
        
        if (transactionId > 0) {
            JSONObject result = new JSONObject();
            result.put("message", "Withdrawal successful");
            result.put("transactionId", transactionId);
            ResponseUtil.success(resp, result);
        } else {
            ResponseUtil.fail(resp, 500, "Failed to record transaction");
        }
    }

    private void handleTransfer(HttpServletResponse resp, UserInfo user, Long amount, String targetCardNo) throws IOException {
        // Check if target user exists
        // Note: This is simplified - in a real app, we'd need more validation
        JSONObject result = new JSONObject();
        
        // Create transaction record for sender
        Transaction senderTransaction = new Transaction();
        senderTransaction.setUserId(user.getId());
        senderTransaction.setTransactionType("transfer_out");
        senderTransaction.setAmount(amount);
        senderTransaction.setBalanceAfter(-amount);
        senderTransaction.setDescription("Transfer to " + targetCardNo);
        senderTransaction.setCreateBy(user.getId());
        senderTransaction.setUpdateBy(user.getId());
        senderTransaction.setCreateTime(Instant.now());
        senderTransaction.setUpdateTime(Instant.now());
        
        // Save sender transaction
        Long senderTxId = transactionDao.addOne(senderTransaction);
        
        // Create transaction record for receiver (if exists)
        // In a real implementation, we would look up the target user
        Transaction receiverTransaction = new Transaction();
        receiverTransaction.setUserId(0L); // Would be set to actual receiver ID
        receiverTransaction.setTransactionType("transfer_in");
        receiverTransaction.setAmount(amount);
        receiverTransaction.setBalanceAfter(amount);
        receiverTransaction.setDescription("Transfer from " + user.getCardNo());
        receiverTransaction.setCreateBy(user.getId());
        receiverTransaction.setUpdateBy(user.getId());
        receiverTransaction.setCreateTime(Instant.now());
        receiverTransaction.setUpdateTime(Instant.now());
        
        // Save receiver transaction
        Long receiverTxId = transactionDao.addOne(receiverTransaction);
        
        if (senderTxId > 0 && receiverTxId > 0) {
            result.put("message", "Transfer successful");
            result.put("senderTransactionId", senderTxId);
            result.put("receiverTransactionId", receiverTxId);
            ResponseUtil.success(resp, result);
        } else {
            ResponseUtil.fail(resp, 500, "Failed to complete transfer");
        }
    }
}