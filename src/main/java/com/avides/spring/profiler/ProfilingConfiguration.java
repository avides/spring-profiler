package com.avides.spring.profiler;

import org.springframework.context.annotation.Bean;

/**
 * @author Martin Schumacher
 */
public class ProfilingConfiguration
{
    @Bean
    public ProfilingExecutor profilingExecutor()
    {
        return new ProfilingExecutor();
    }

    @Bean
    public LoggingProfilingHandler loggingProfilingHandler()
    {
        return new LoggingProfilingHandler();
    }

    @Bean
    public SysoutProfilingHandler sysoutProfilingHandler()
    {
        return new SysoutProfilingHandler();
    }
}