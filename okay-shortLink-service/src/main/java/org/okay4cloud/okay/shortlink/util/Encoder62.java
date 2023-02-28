package org.okay4cloud.okay.shortlink.util;

/**
 * @author wnhyang
 * @date 2022-08-22 15:47
 **/
public class Encoder62 {

    /**
     * BloomFilter预期插入次数
     * 1L << 16 << 16
     */
    public static final long SIZE = 1L << 16;

    public static final int SCALE = 62;

    private static final char[] ARRAYS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 十进制转62进制
     *
     * @param num long类型十进制
     * @return 62进制字符串
     */
    public static String encode62(long num) {
        StringBuilder sb = new StringBuilder();

        int remain = 0;
        while (num > 0) {
            remain = (int) (num % SCALE);
            sb.append(ARRAYS[remain]);
            num /= SCALE;
        }

        return sb.reverse().toString();
    }
}
