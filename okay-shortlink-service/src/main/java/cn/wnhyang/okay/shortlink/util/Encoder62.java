package cn.wnhyang.okay.shortlink.util;

import lombok.experimental.UtilityClass;

/**
 * @author wnhyang
 * @date 2022-08-22 15:47
 **/
@UtilityClass
public class Encoder62 {

    /**
     * BloomFilter预期插入次数
     * 1L << 16 << 16
     */
    public final long SIZE = 1L << 16;

    public final int SCALE = 62;

    private final char[] ARRAYS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 十进制转62进制
     *
     * @param num long类型十进制
     * @return 62进制字符串
     */
    public String encode62(long num) {
        StringBuilder sb = new StringBuilder();

        int remain;
        while (num > 0) {
            remain = (int) (num % SCALE);
            sb.append(ARRAYS[remain]);
            num /= SCALE;
        }

        return sb.reverse().toString();
    }
}
