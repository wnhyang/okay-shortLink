package org.okay4cloud.okay.shortlink.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.okay4cloud.okay.common.core.util.R;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * @author wnhyang
 * @date 2023/5/19
 **/
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogAspect {

    private final ObjectMapper objectMapper;

    /**
     * 定义切点，匹配所有controller包下面所有公共方法
     */
    @Pointcut("execution(public * org.okay4cloud.okay.shortlink.controller..*.*(..))")
    public void executionLog() {
    }

    /**
     * 通过环绕通知拦截请求参数与返回结果
     *
     * @param pjp 切点
     */
    @Around("executionLog()")
    public Object around(ProceedingJoinPoint pjp) throws JsonProcessingException {

        // 记录请求开始时间
        long startTime = System.currentTimeMillis();

        // 获取当前请求对象并记录请求参数
        logRequestParams(pjp);

        // 执行请求内容，并处理响应结果
        Object result;
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            log.error("error occurred while processing the request: {}", throwable.getMessage());
            return R.failed();
        }

        // 记录响应结果
        logResponseResult(result);

        // 记录请求结束时间
        long endTime = System.currentTimeMillis();

        // 输出耗时信息
        log.info("\n{} 请求处理结束，耗时：{}ms", pjp.getSignature(), (endTime - startTime));
        return result;
    }

    /**
     * 记录请求参数
     */
    private void logRequestParams(ProceedingJoinPoint pjp) throws JsonProcessingException {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String[] argNames = methodSignature.getParameterNames();
        Object[] argValues = pjp.getArgs();
        StringBuilder sb = new StringBuilder();
        sb.append("\n------------------------------------------");
        sb.append("\nMethod Name : ").append(methodSignature.getName());
        sb.append("\nURL         : ").append(request.getRequestURI());
        sb.append("\nHTTP Method : ").append(request.getMethod());

        Map<String, Object> args = Maps.newHashMapWithExpectedSize(argValues.length);
        for (int i = 0; i < argNames.length; i++) {
            String argName = argNames[i];
            Object argValue = argValues[i];
            // 被忽略时，标记为 ignore 字符串，避免和 null 混在一起
            args.put(argName, !isIgnoreArgs(argValue) ? argValue : "[ignore]");
        }
        sb.append("\n").append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(args));
        sb.append("\n==========================================");
        log.info(sb.toString());
    }

    /**
     * 记录响应结果
     */
    private void logResponseResult(Object result) throws JsonProcessingException {
        if (result == null) {
            return;
        }

        // 将响应结果转换为 JSON 字符串，并打印
        String resultJsonString;
        if (result instanceof R) {
            resultJsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        } else {
            resultJsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(R.ok(result));
        }

        log.info("\n------------------------------------------" +
                "\nResponse :" + resultJsonString +
                "\n==========================================");
    }

    private static boolean isIgnoreArgs(Object object) {
        Class<?> clazz = object.getClass();
        // 处理数组的情况
        if (clazz.isArray()) {
            return IntStream.range(0, Array.getLength(object))
                    .anyMatch(index -> isIgnoreArgs(Array.get(object, index)));
        }
        // 递归，处理数组、Collection、Map 的情况
        if (Collection.class.isAssignableFrom(clazz)) {
            return ((Collection<?>) object).stream()
                    .anyMatch((Predicate<Object>) LogAspect::isIgnoreArgs);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return isIgnoreArgs(((Map<?, ?>) object).values());
        }
        // obj
        return object instanceof MultipartFile
                || object instanceof HttpServletRequest
                || object instanceof HttpServletResponse
                || object instanceof BindingResult;
    }

}
