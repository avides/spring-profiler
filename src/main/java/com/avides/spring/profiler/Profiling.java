package com.avides.spring.profiler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Martin Schumacher
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Profiling
{
    String value() default "";

    long allowedMillis() default 0;

    Class<? extends ProfilingHandler>[] profilingHandlerBeanClasses() default LoggingProfilingHandler.class;
}