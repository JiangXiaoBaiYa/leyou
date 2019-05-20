package com.leyou.common.threadlocals;

/**
 * @Author: 姜光明
 * @Date: 2019/5/20 9:44
 * <p>
 * 定义容器，存储用户数据,保证每个线程都有自己的用户资源，互不干扰
 */
public class UserHolder {
    private static final ThreadLocal<Long> TL = new ThreadLocal<>();

    public static void setUser(Long userId) {
        TL.set(userId);
    }

    public static Long getUser() {
        return TL.get();
    }

    public static void removeUser() {
        TL.remove();
    }
}
