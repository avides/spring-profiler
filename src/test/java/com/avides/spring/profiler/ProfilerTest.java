package com.avides.spring.profiler;

import static com.avides.spring.profiler.Profiler.profile;
import static com.avides.spring.profiler.Profiler.startProfiling;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.avides.spring.profiler.Profiler.CallableProfilingResult;

/**
 * @author Martin Schumacher
 */
public class ProfilerTest
{
    @Test
    public void testProfiling() throws InterruptedException
    {
        Profiler profiler = startProfiling("anyIdentifier", asList(new SysoutProfilingHandler(), new LoggingProfilingHandler()));
        assertThat(profiler.getDurationMillis()).isEqualTo(-1);
        assertThat(profiler.getAllowedMillis()).isEqualTo(-1);
        assertThat(profiler.getExceedingMillis()).isEqualTo(-1);
        assertThat(profiler.getFallBelowMillis()).isEqualTo(-1);
        assertThat(profiler.exceedsAllowedMillis()).isFalse();
        Thread.sleep(40);
        profiler.stop();
        assertThat(profiler.getDurationMillis()).isGreaterThanOrEqualTo(40);
        assertThat(profiler.getAllowedMillis()).isEqualTo(-1);
        assertThat(profiler.getExceedingMillis()).isEqualTo(-1);
        assertThat(profiler.getFallBelowMillis()).isEqualTo(-1);
        assertThat(profiler.exceedsAllowedMillis()).isFalse();
        Thread.sleep(40);
        profiler.stop(150);
        assertThat(profiler.getDurationMillis()).isGreaterThanOrEqualTo(80);
        assertThat(profiler.getAllowedMillis()).isEqualTo(150);
        assertThat(profiler.getExceedingMillis()).isEqualTo(-1);
        assertThat(profiler.getFallBelowMillis()).isGreaterThanOrEqualTo(0);
        assertThat(profiler.exceedsAllowedMillis()).isFalse();
        Thread.sleep(100);
        profiler.stop(150);
        assertThat(profiler.getDurationMillis()).isGreaterThanOrEqualTo(180);
        assertThat(profiler.getAllowedMillis()).isEqualTo(150);
        assertThat(profiler.getExceedingMillis()).isGreaterThanOrEqualTo(0);
        assertThat(profiler.getFallBelowMillis()).isEqualTo(-1);
        assertThat(profiler.exceedsAllowedMillis()).isTrue();
    }

    @Test
    public void testProfilingWithVarArgs() throws InterruptedException
    {
        Profiler profiler = startProfiling("anyIdentifier", new SysoutProfilingHandler(), new LoggingProfilingHandler());
        Thread.sleep(40);
        assertThat(profiler.stop().getDurationMillis()).isGreaterThanOrEqualTo(40);
        Thread.sleep(40);
        assertThat(profiler.stop(150).getDurationMillis()).isGreaterThanOrEqualTo(80);
        Thread.sleep(100);
        assertThat(profiler.stop(150).getDurationMillis()).isGreaterThanOrEqualTo(180);
    }

    @Test
    public void testProfilingWithoutHandlers() throws InterruptedException
    {
        Profiler profiler = startProfiling("anyIdentifier");
        Thread.sleep(40);
        assertThat(profiler.stop().getDurationMillis()).isGreaterThanOrEqualTo(40);
        Thread.sleep(40);
        assertThat(profiler.stop(150).getDurationMillis()).isGreaterThanOrEqualTo(80);
        Thread.sleep(100);
        assertThat(profiler.stop(150).getDurationMillis()).isGreaterThanOrEqualTo(180);
    }

    @Test
    public void testProfileWithRunnable()
    {
        Profiler profiler = profile("anyId", () ->
        {
            try
            {
                Thread.sleep(40);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });
        assertThat(profiler.getDurationMillis()).isGreaterThanOrEqualTo(40);
        assertThat(profiler.getAllowedMillis()).isEqualTo(-1);
        assertThat(profiler.getExceedingMillis()).isEqualTo(-1);
        assertThat(profiler.getFallBelowMillis()).isEqualTo(-1);
        assertThat(profiler.exceedsAllowedMillis()).isFalse();
    }

    @Test
    public void testProfileWithRunnableAndAllowedMillis()
    {
        Profiler profiler = profile("anyId", 20, () ->
        {
            try
            {
                Thread.sleep(40);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });
        assertThat(profiler.getDurationMillis()).isGreaterThanOrEqualTo(40);
        assertThat(profiler.getAllowedMillis()).isEqualTo(20);
        assertThat(profiler.getExceedingMillis()).isGreaterThanOrEqualTo(20);
        assertThat(profiler.getFallBelowMillis()).isEqualTo(-1);
        assertThat(profiler.exceedsAllowedMillis()).isTrue();
    }

    @Test
    public void testProfileWithCallable() throws Throwable
    {
        CallableProfilingResult<String> result = profile("anyId", () ->
        {
            Thread.sleep(40);
            return "anyResult";
        });
        assertThat(result.getProfiler().getDurationMillis()).isGreaterThanOrEqualTo(40);
        assertThat(result.getProfiler().getAllowedMillis()).isEqualTo(-1);
        assertThat(result.getProfiler().getExceedingMillis()).isEqualTo(-1);
        assertThat(result.getProfiler().getFallBelowMillis()).isEqualTo(-1);
        assertThat(result.getProfiler().exceedsAllowedMillis()).isFalse();
        assertThat(result.getResult()).isEqualTo("anyResult");
    }

    @Test
    public void testProfileWithCallableAndAllowedMillis() throws Throwable
    {
        CallableProfilingResult<String> result = profile("anyId", 20, () ->
        {
            Thread.sleep(40);
            return "anyResult";
        });
        assertThat(result.getProfiler().getDurationMillis()).isGreaterThanOrEqualTo(40);
        assertThat(result.getProfiler().getAllowedMillis()).isEqualTo(20);
        assertThat(result.getProfiler().getExceedingMillis()).isGreaterThanOrEqualTo(20);
        assertThat(result.getProfiler().getFallBelowMillis()).isEqualTo(-1);
        assertThat(result.getProfiler().exceedsAllowedMillis()).isTrue();
        assertThat(result.getResult()).isEqualTo("anyResult");
    }

    @Test(expected = RuntimeException.class)
    public void testProfileWithCallableAndException() throws Throwable
    {
        profile("anyId", () ->
        {
            Thread.sleep(40);
            throw new RuntimeException("anyException");
        });
    }
}