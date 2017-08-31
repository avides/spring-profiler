package com.avides.spring.profiler;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Martin Schumacher
 */
public abstract class AbstractProfilingHandler implements ProfilingHandler
{
    public static String generateLoggingMessage(String id, Map<String, Object> args, long duration, long allowedMillis)
    {
        StringBuilder loggingBuilder = new StringBuilder("execution time for ").append(id).append(": ").append(duration).append("ms");
        if (allowedMillis > 0)
        {
            loggingBuilder.append(" (allowed: ").append(allowedMillis).append("ms)");
        }
        if (!args.isEmpty())
        {
            loggingBuilder.append("\n\tgiven parameters:");
            for (Entry<String, Object> entry : args.entrySet())
            {
                loggingBuilder.append("\n\t").append(entry.getKey()).append(": ").append(String.valueOf(entry.getValue()));
            }
        }
        return loggingBuilder.toString();
    }
}