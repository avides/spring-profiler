package com.avides.spring.profiler;

import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

/**
 * @author Martin Schumacher
 */
public class Profiler
{
    private static final ExecutorService executor = newSingleThreadExecutor();

    private String id;
    private long start;
    private Collection<ProfilingHandler> profilingHandlers;

    private Profiler(String id, Collection<ProfilingHandler> profilingHandlers)
    {
        this.id = id;
        this.profilingHandlers = profilingHandlers;
    }

    private Profiler start()
    {
        start = currentTimeMillis();
        return this;
    }

    public void stop(long allowedMillis)
    {
        long duration = currentTimeMillis() - start;
        executor.execute(() -> profilingHandlers.forEach(profilingHandler -> profilingHandler.profile(id, start, duration, allowedMillis)));
    }

    public void stop()
    {
        stop(0);
    }

    public static Profiler startProfiling(String id, Collection<ProfilingHandler> profilingHandlers)
    {
        return new Profiler(id, profilingHandlers).start();
    }

    public static Profiler startProfiling(String id, ProfilingHandler... profilingHandlers)
    {
        return new Profiler(id, asList(profilingHandlers)).start();
    }

    public static Profiler startProfiling(String id)
    {
        return startProfiling(id, singletonList(new LoggingProfilingHandler()));
    }

    static String generateLoggingMessage(String id, long duration, long allowedMillis)
    {
        StringBuilder loggingBuilder = new StringBuilder("execution time for ").append(id).append(": ").append(duration).append("ms");
        if (allowedMillis > 0)
        {
            loggingBuilder.append(" (allowed: ").append(allowedMillis).append("ms)");
        }
        return loggingBuilder.toString();
    }
}