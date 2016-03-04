package com.avides.spring.profiler;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

/**
 * @author Martin Schumacher
 */
public class LoggingProfilingHandler extends AbstractProfilingHandler
{
    private static final Logger log = getLogger(LoggingProfilingHandler.class);

    @Override
    public void profile(String id, long start, long duration, long allowedMillis)
    {
        if ((allowedMillis > -1) && (duration > allowedMillis))
        {
            log.warn(generateLoggingMessage(id, duration, allowedMillis));
        }
        else
        {
            log.debug(generateLoggingMessage(id, duration, allowedMillis));
        }
    }
}