package com.avides.spring.profiler;

import java.util.Map;

/**
 * @author Martin Schumacher
 */
public interface ProfilingHandler
{
    void profile(String id, Map<String, Object> args, long start, long duration, long allowedMillis);
}