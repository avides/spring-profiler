package com.avides.spring.profiler;

import static com.avides.spring.profiler.Profiler.profile;
import static com.avides.spring.profiler.ProfilerUtils.collectProfilingHandlers;
import static com.avides.spring.profiler.ProfilerUtils.getIdentifier;
import static com.avides.spring.profiler.ProfilerUtils.getMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    private ApplicationContext context;

    @Around("@annotation(com.avides.spring.profiler.Profiling)")
    public Object handleProfiledMethod(ProceedingJoinPoint pjp) throws Throwable
    {
        Method method = getMethod(pjp);
        Profiling profiling = method.getAnnotation(Profiling.class);
        String[] outputArgs = profiling.outputArgs();
        Map<String, Object> args = new HashMap<>();
        if (outputArgs.length > 0)
        {
            for (String outputArg : outputArgs)
            {
                for (int i = 0; i < method.getParameters().length; i++)
                {
                    if (method.getParameters()[i].getName().equals(outputArg))
                    {
                        args.put(outputArg, pjp.getArgs()[i]);
                    }
                }
            }
        }
        return profile(getIdentifier(method), profiling.allowedMillis(), () -> pjp.proceed(), args, collectProfilingHandlers(context, profiling)).getResult();
    }
}