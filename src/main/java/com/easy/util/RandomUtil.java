package com.easy.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具
 *
 * @author daimao
 * @date 2024-12-18 10:14
 */
public class RandomUtil {
    private RandomUtil() {

    }

    /**
     * 生成随机数
     * 随机数范围[start,end] 闭区间
     *
     * @param start 开始
     * @param end   结束
     * @return 随机数
     */
    public static long randomLong(long start, long end) {
        if (start > end) {
            throw new IllegalArgumentException("随机数参数违法，结束值必须大于起始值");
        }
        return ThreadLocalRandom.current().nextLong(start, end + 1);
    }

    /**
     * 生成随机数
     * 随机数范围[start,end] 闭区间
     *
     * @param start 开始
     * @param end   结束
     * @return 随机数
     */
    public static int randomInt(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("随机数参数违法，结束值必须大于起始值");
        }
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }
}