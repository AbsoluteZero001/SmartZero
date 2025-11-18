package com.banksystem.application.utills;

import com.banksystem.application.entity.AdminInfo;
import com.banksystem.application.entity.UserInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 通用工具类，提供各种数据转换和生成功能
 */
public class ConvertUtils {
    /**
     * 将时间字符串转换为Instant对象
     * @param time 时间字符串，格式为"yyyy-MM-dd HH:mm:ss"
     * @return 转换后的Instant对象
     */

    public static Instant toInstant(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date.toInstant();
    }

    /**
     * 生成指定长度的随机字符串
     * @param num 要生成的字符串长度
     * @return 随机生成的字符串
     */
    public static String getCookie(int num) {
        String string = "abcdefjhijkmlnopqrstuvwxyz";
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            int index = random.nextInt(string.length());
            sb.append(string.charAt(index));
        }
        return sb.toString();
    }

    /**
     *管理员登录map
     */
    public static Map<String, AdminInfo> ADMIN_LOGIN_MAP = new HashMap<>();
    /**
     * 用户登录map
     */
    public static Map<String, UserInfo> USER_LOGIN_MAP = new HashMap<>();

    public static void main(String[] args) {
        System.out.println(getCookie(12));
    }
}
