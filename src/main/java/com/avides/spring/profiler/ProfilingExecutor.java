package com.avides.spring.profiler;

import static com.avides.spring.profiler.Profiler.startProfiling;
import static com.avides.spring.profiler.ProfilerUtils.collectProfilingHandlers;
import static com.avides.spring.profiler.ProfilerUtils.getParameterTypes;

import java.lang.reflect.Method;
import java.util.Collection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        if (method.getDeclaringClass().isInterface())
        {
            method = pjp.getTarget().getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        }
        Profiling profiling = method.getAnnotation(Profiling.class);
        String id = profiling.value();
        if (id.isEmpty())
        {
            id = "method " + method.getDeclaringClass().getName() + "." + method.getName() + "(" + getParameterTypes(method) + ")";
        }
        long allowedMillis = profiling.allowedMillis();
        Collection<ProfilingHandler> profilingHandlers = collectProfilingHandlers(context, profiling);
        Profiler profiler = startProfiling(id, profilingHandlers);
        Object result = pjp.proceed();
        profiler.stop(allowedMillis);
        return result;
    }
}