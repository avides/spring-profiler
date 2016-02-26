package com.avides.spring.profiler;

/**
 * @author Martin Schumacher
 */
public interface ProfilingHandler
{
    void profile(String id, long start, long duration, long allowedMillis);
}