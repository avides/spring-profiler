package com.avides.spring.profiler;

import static com.avides.spring.profiler.Profiler.startProfiling;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

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
        assertThat(profiler).isNotNull();
        Thread.sleep(40);
        profiler.stop();
        Thread.sleep(40);
        profiler.stop(150);
        Thread.sleep(100);
        profiler.stop(150);
    }

    @Test
    public void testProfilingWithVarArgs() throws InterruptedException
    {
        Profiler profiler = startProfiling("anyIdentifier", new SysoutProfilingHandler(), new LoggingProfilingHandler());
        assertThat(profiler).isNotNull();
        Thread.sleep(40);
        profiler.stop();
        Thread.sleep(40);
        profiler.stop(150);
        Thread.sleep(100);
        profiler.stop(150);
    }

    @Test
    public void testProfilingWithoutHandlers() throws InterruptedException
    {
        Profiler profiler = startProfiling("anyIdentifier");
        assertThat(profiler).isNotNull();
        Thread.sleep(40);
        profiler.stop();
        Thread.sleep(40);
        profiler.stop(150);
        Thread.sleep(100);
        profiler.stop(150);
    }
}