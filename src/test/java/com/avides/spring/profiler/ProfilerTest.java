package com.avides.spring.profiler;

import static com.avides.spring.profiler.Profiler.startProfiling;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Martin Schumacher
 */
public class ProfilerTest
{
    @Test
    public void testProfiling() throws InterruptedException
    {
        Profiler profiler = startProfiling("anyIdentifier", asList(new SysoutProfilingHandler(), new LoggingProfilingHandler()));
        Thread.sleep(40);
        profiler.stop();
        Thread.sleep(40);
        profiler.stop(150);
        Thread.sleep(40);
        profiler.stop(150);
    }

    @Test
    public void testProfilingWithVarArgs() throws InterruptedException
    {
        Profiler profiler = startProfiling("anyIdentifier", new SysoutProfilingHandler(), new LoggingProfilingHandler());
        Thread.sleep(40);
        profiler.stop();
        Thread.sleep(40);
        profiler.stop(150);
        Thread.sleep(40);
        profiler.stop(150);
    }

    @Test
    public void testProfilingWithoutHandlers() throws InterruptedException
    {
        Profiler profiler = startProfiling("anyIdentifier");
        Thread.sleep(40);
        profiler.stop();
        Thread.sleep(40);
        profiler.stop(150);
        Thread.sleep(40);
        profiler.stop(150);
    }

    @Test
    public void testGetParameterTypesFromMethod() throws NoSuchMethodException, SecurityException
    {
        assertEquals("java.lang.String, boolean", ProfilingExecutor.getParameterTypes(this.getClass().getMethod("testMethod", String.class, boolean.class)));
    }

    public void testMethod(String arg1, boolean arg2)
    {
        // nothing to do here, only for test-purposes
    }
}