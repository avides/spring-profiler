package com.avides.spring.profiler;

/**
 * @author Martin Schumacher
 */
public class SysoutProfilingHandler extends AbstractProfilingHandler
{
    @Override
    public void profile(String id, long start, long duration, long allowedMillis)
    {
        if ((allowedMillis > 0) && (duration > allowedMillis))
        {
            System.out.println("WARNING: " + generateLoggingMessage(id, duration, allowedMillis));
        }
        else
        {
            System.out.println(generateLoggingMessage(id, duration, allowedMillis));
        }
    }
}