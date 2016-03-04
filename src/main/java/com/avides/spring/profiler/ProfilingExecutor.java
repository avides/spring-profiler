package com.avides.spring.profiler;

import static com.avides.spring.profiler.Profiler.profile;
import static com.avides.spring.profiler.ProfilerUtils.collectProfilingHandlers;
import static com.avides.spring.profiler.ProfilerUtils.getIdentifier;
import static com.avides.spring.profiler.ProfilerUtils.getMethod;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author Martin Schumacher
 */
@Aspect
public class ProfilingExecutor
{
    private @Autowired ApplicationContext context;

    @Around("@annotation(com.avides.spring.profiler.Profiling)")
    public Object handleProfiledMethod(ProceedingJoinPoint pjp) throws Throwable
    {
        Method method = getMethod(pjp);
        Profiling profiling = method.getAnnotation(Profiling.class);
        return profile(getIdentifier(method), profiling.allowedMillis(), () -> pjp.proceed(), collectProfilingHandlers(context, profiling)).getResult();
    }
}