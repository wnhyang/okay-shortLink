package org.okay4cloud.okay.shortlink.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.okay4cloud.okay.common.core.util.RedisUtils;
import org.okay4cloud.okay.shortlink.annotation.Visit;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author wnhyang
 * @date 2023/3/11
 **/
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class VisitAspect {

    private final ValueOperations<String, String> valueOperations;

    @After("@annotation(visit)")
    public void after(JoinPoint point, Visit visit) {
        log.info("value {},prefix {},timeout {},unit {}", visit.value(), visit.prefix(), visit.timeout(), visit.unit());
        Object[] args = point.getArgs();
        String now = LocalDate.now().toString();
        // args这么放，那么就不具备普适性了
        String key = RedisUtils.getKey(visit.prefix(), args[0], now);
        Object value = valueOperations.get(key);
        if (null != value) {
            valueOperations.increment(key);
        } else {
            valueOperations.set(key, "1", visit.timeout(), visit.unit());
        }
    }
}
