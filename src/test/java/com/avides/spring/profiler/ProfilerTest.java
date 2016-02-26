package com.avides.spring.profiler;

import static com.avides.spring.profiler.Profiler.startProfiling;
import static java.util.Arrays.asList;

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
}