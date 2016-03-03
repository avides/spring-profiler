package com.avides.spring.profiler;

/**
 * @author Martin Schumacher
 */
public abstract class AbstractProfilingHandler implements ProfilingHandler
{
    public static String generateLoggingMessage(String id, long duration, long allowedMillis)
    {
        StringBuilder loggingBuilder = new StringBuilder("execution time for ").append(id).append(": ").append(duration).append("ms");
        if (allowedMillis > 0)
        {
            loggingBuilder.append(" (allowed: ").append(allowedMillis).append("ms)");
        }
        return loggingBuilder.toString();
    }
}