package cn.wnhyang.okay.shortlink.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 访问量记录，适合get接口，且只有一个参数
 *
 * @author wnhyang
 * @date 2023/3/11
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Visit {
    /**
     * 描述
     *
     * @return String
     */
    String value();

    /**
     * 缓存前缀
     *
     * @return String
     */
    String prefix();

    /**
     * 缓存过期时间
     *
     * @return long
     */
    long timeout() default 7L;

    /**
     * 缓存过期时间单位
     *
     * @return TimeUnit
     */
    TimeUnit unit() default TimeUnit.DAYS;
}
