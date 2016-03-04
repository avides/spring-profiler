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
    private long durationMillis = -1;
    private long allowedMillis = -1;

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

    public Profiler stop(long allowedMillis)
    {
        this.allowedMillis = allowedMillis;
        durationMillis = currentTimeMillis() - start;
        executor.execute(() -> profilingHandlers.forEach(profilingHandler -> profilingHandler.profile(id, start, durationMillis, allowedMillis)));
        return this;
    }

    public Profiler stop()
    {
        return stop(-1);
    }

    public long getDurationMillis()
    {
        return durationMillis;
    }

    public long getAllowedMillis()
    {
        return allowedMillis;
    }

    public long getExceedingMillis()
    {
        long difference = durationMillis - allowedMillis;
        if ((allowedMillis > -1) && (durationMillis > -1) && (difference > 0))
        {
            return difference;
        }
        return -1;
    }

    public long getFallBelowMillis()
    {
        long difference = durationMillis - allowedMillis;
        if ((allowedMillis > -1) && (durationMillis > -1) && (difference < 0))
        {
            return -difference;
        }
        return -1;
    }

    public boolean exceedsAllowedMillis()
    {
        return getExceedingMillis() > 0;
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

    public static <T> CallableProfilingResult<T> profile(String id, long allowedMillis, ThrowingCallable<T> callable,
        Collection<ProfilingHandler> profilingHandlers) throws Throwable
    {
        Profiler profiler = startProfiling(id, profilingHandlers);
        try
        {
            CallableProfilingResult<T> result = new CallableProfilingResult<T>(callable.call());
            result.setProfiler(profiler.stop(allowedMillis));
            return result;
        }
        catch (Throwable e)
        {
            profiler.stop(allowedMillis);
            throw e;
        }
    }

    public static <T> CallableProfilingResult<T> profile(String id, long allowedMillis, ThrowingCallable<T> callable, ProfilingHandler... profilingHandlers)
        throws Throwable
    {
        return profile(id, allowedMillis, callable, asList(profilingHandlers));
    }

    public static <T> CallableProfilingResult<T> profile(String id, long allowedMillis, ThrowingCallable<T> callable) throws Throwable
    {
        return profile(id, allowedMillis, callable, new LoggingProfilingHandler());
    }

    public static <T> CallableProfilingResult<T> profile(String id, ThrowingCallable<T> callable, Collection<ProfilingHandler> profilingHandlers)
        throws Throwable
    {
        return profile(id, -1, callable, profilingHandlers);
    }

    public static <T> CallableProfilingResult<T> profile(String id, ThrowingCallable<T> callable, ProfilingHandler... profilingHandlers) throws Throwable
    {
        return profile(id, callable, asList(profilingHandlers));
    }

    public static <T> CallableProfilingResult<T> profile(String id, ThrowingCallable<T> callable) throws Throwable
    {
        return profile(id, callable, new LoggingProfilingHandler());
    }

    public static Profiler profile(String id, long allowedMillis, Runnable runnable, Collection<ProfilingHandler> profilingHandlers)
    {
        Profiler profiler = startProfiling(id, profilingHandlers);
        runnable.run();
        return profiler.stop(allowedMillis);
    }

    public static Profiler profile(String id, long allowedMillis, Runnable runnable, ProfilingHandler... profilingHandlers)
    {
        return profile(id, allowedMillis, runnable, asList(profilingHandlers));
    }

    public static Profiler profile(String id, long allowedMillis, Runnable runnable)
    {
        return profile(id, allowedMillis, runnable, new LoggingProfilingHandler());
    }

    public static Profiler profile(String id, Runnable runnable, Collection<ProfilingHandler> profilingHandlers)
    {
        return profile(id, -1, runnable, profilingHandlers);
    }

    public static Profiler profile(String id, Runnable runnable, ProfilingHandler... profilingHandlers)
    {
        return profile(id, runnable, asList(profilingHandlers));
    }

    public static Profiler profile(String id, Runnable runnable)
    {
        return profile(id, runnable, new LoggingProfilingHandler());
    }

    public static interface ThrowingCallable<T>
    {
        T call() throws Throwable;
    }

    public static class CallableProfilingResult<T>
    {
        private Profiler profiler;
        private T result;

        public CallableProfilingResult(T result)
        {
            this.result = result;
        }

        public void setProfiler(Profiler profiler)
        {
            this.profiler = profiler;
        }

        public Profiler getProfiler()
        {
            return profiler;
        }

        public T getResult()
        {
            return result;
        }
    }
}