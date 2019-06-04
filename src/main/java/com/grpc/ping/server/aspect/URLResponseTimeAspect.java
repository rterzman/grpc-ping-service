package com.grpc.ping.server.aspect;

import com.grpc.ping.server.service.URLConnectionReader;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StopWatch;

@Aspect
public class URLResponseTimeAspect {

    @Pointcut("execution(* *.getConnectionWithResponseTime(..))")
    public void getResponseTime() {}

    @Around(value = "getResponseTime()")
    public Object checkResponseTime(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        final Object proceed = pjp.proceed();
        stopWatch.stop();

        return ((URLConnectionReader.URLConnectionWithResponseTime) proceed).toBuilder()
                .responseTime(stopWatch.getLastTaskTimeMillis())
                .build();

    }

}
