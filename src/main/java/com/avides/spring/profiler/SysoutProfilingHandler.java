package com.avides.spring.profiler;

import java.util.Map;

/**
 * @author Martin Schumacher
 */
public class SysoutProfilingHandler extends AbstractProfilingHandler
{
    @Override
    public void profile(String id, Map<String, Object> args, long start, long duration, long allowedMillis)
    {
        if ((allowedMillis > -1) && (duration > allowedMillis))
        {
            System.out.println("WARNING: " + generateLoggingMessage(id, args, duration, allowedMillis));
        }
        else
        {
            System.out.println(generateLoggingMessage(id, args, duration, allowedMillis));
        }
    }
}