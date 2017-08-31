package com.avides.spring.profiler;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Activates profiling of the method where this annotation is set
 *
 * @author Martin Schumacher
 */
@Retention(RUNTIME)
@Target(METHOD)
@Inherited
public @interface Profiling
{
    /**
     * Identifier for this profiling, defaults to the annotated method-name
     *
     * @return the identifier-name
     */
    String value() default "";

    /**
     * Allowed time for execution in milliseconds. If this value is exceeded,
     * debug-messages will turn into warn-messages
     *
     * @return allowed execution-time in milliseconds
     */
    long allowedMillis() default -1;

    /**
     * Array of LoggingHandlerClasses that should handle the profiling (and must
     * be in the ApplicationContext), default is the LoggingProfilingHandler
     *
     * @return Array of LoggingHandlerClasses
     */
    Class<? extends ProfilingHandler>[] profilingHandlerBeanClasses() default LoggingProfilingHandler.class;

    String[] outputArgs() default
    {};
}