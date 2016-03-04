package com.avides.spring.profiler;

import static org.springframework.util.ReflectionUtils.makeAccessible;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;

/**
 * @author Martin Schumacher
 */
public abstract class ProfilerUtils
{
    public static Method getMethod(ProceedingJoinPoint pjp) throws NoSuchMethodException, SecurityException
    {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        if (method.getDeclaringClass().isInterface())
        {
            method = pjp.getTarget().getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        }
        return method;
    }

    public static String getIdentifier(Method method)
    {
        makeAccessible(method);
        Profiling profiling = method.getAnnotation(Profiling.class);
        String id = profiling.value();
        if (id.isEmpty())
        {
            id = "method " + method.getDeclaringClass().getName() + "." + method.getName() + "(" + getParameterTypes(method) + ")";
        }
        return id;
    }

    public static String getParameterTypes(Method method)
    {
        StringBuilder builder = new StringBuilder();
        for (Class<?> type : method.getParameterTypes())
        {
            if (builder.length() > 0)
            {
                builder.append(", ");
            }
            builder.append(type.getName());
        }
        return builder.toString();
    }

    public static Collection<ProfilingHandler> collectProfilingHandlers(ApplicationContext context, Profiling profiling)
    {
        Class<? extends ProfilingHandler>[] profilingHandlerClasses = profiling.profilingHandlerBeanClasses();
        List<ProfilingHandler> profilingHandlers = new ArrayList<>();
        for (Class<? extends ProfilingHandler> profilingHandlerClass : profilingHandlerClasses)
        {
            ProfilingHandler profilingHandler = context.getBean(profilingHandlerClass);
            profilingHandlers.add(profilingHandler);
        }
        return profilingHandlers;
    }
}