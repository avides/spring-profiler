package com.avides.spring.profiler;

import static com.avides.spring.profiler.Profiler.generateLoggingMessage;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

/**
 * @author Martin Schumacher
 */
public class LoggingProfilingHandler implements ProfilingHandler
{
    private static final Logger log = getLogger(LoggingProfilingHandler.class);

    @Override
    public void profile(String id, long start, long duration, long allowedMillis)
    {
        if ((allowedMillis > 0) && (duration > allowedMillis))
        {
            if (log.isWarnEnabled())
            {
                log.warn(generateLoggingMessage(id, duration, allowedMillis));
            }
        }
        else if (log.isDebugEnabled())
        {
            log.debug(generateLoggingMessage(id, duration, allowedMillis));
        }
    }
}