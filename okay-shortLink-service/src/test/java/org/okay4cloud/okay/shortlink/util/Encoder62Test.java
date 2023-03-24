package org.okay4cloud.okay.shortlink.util;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Random;

@Slf4j
class Encoder62Test {
    private static final char ch[] = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01".toCharArray();
    private static final Random random = new Random();
    private static final HashFunction HASH_FUNCTION = Hashing.murmur3_32_fixed();

    private static final long INT_MAX = (1L << 32) - 1;

    @Test
    public void test() {
        Long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String randomString = createRandomString(6);
//            System.out.println(randomString);
        }
        Long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        Long startTime2 = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String s = randomString();
//            System.out.println(s);
        }

        Long endTime2 = System.currentTimeMillis();
        System.out.println(endTime2 - startTime2);

    }

    private String randomString() {
        long l = random.nextLong();
        long n = l & (INT_MAX);
        return Encoder62.encode62(n);
    }

    private String createRandomString(int length) {
        if (length > 0) {
            int index = 0;
            char[] temp = new char[length];
            int num = random.nextInt();
            for (int i = 0; i < length % 5; i++) {
                temp[index++] = ch[num & 63];
                num >>= 6;
            }
            for (int i = 0; i < length / 5; i++) {
                num = random.nextInt();
                for (int j = 0; j < 5; j++) {
                    temp[index++] = ch[num & 63];
                    num >>= 6;
                }
            }
            return new String(temp, 0, length);
        } else if (length == 0) {
            return "";
        } else {
            throw new IllegalArgumentException();
        }
    }

}