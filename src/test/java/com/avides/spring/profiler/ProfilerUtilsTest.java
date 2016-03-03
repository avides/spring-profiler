package com.avides.spring.profiler;

import static com.avides.spring.profiler.ProfilerUtils.collectProfilingHandlers;
import static com.avides.spring.profiler.ProfilerUtils.getParameterTypes;
import static org.fest.assertions.Assertions.assertThat;
import static org.powermock.api.easymock.PowerMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.resetAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

/**
 * @author Martin Schumacher
 */
@RunWith(PowerMockRunner.class)
public class ProfilerUtilsTest
{
    private @Mock ApplicationContext context;

    @After
    public void tearDown()
    {
        resetAll();
    }

    @Test
    public void testGetParameterTypesFromMethod() throws NoSuchMethodException, SecurityException
    {
        assertThat(getParameterTypes(this.getClass().getMethod("testMethod", String.class, boolean.class))).isEqualTo("java.lang.String, boolean");
    }

    @Test
    public void testCollectProfilingHandlers() throws NoSuchMethodException, SecurityException
    {
        Method testMethod = this.getClass().getMethod("testMethod", String.class, boolean.class);
        Profiling profiling = testMethod.getAnnotation(Profiling.class);
        LoggingProfilingHandler loggingProfilingHandler = new LoggingProfilingHandler();

        context.getBean(LoggingProfilingHandler.class);
        expectLastCall().andReturn(loggingProfilingHandler);

        replayAll();
        assertThat(collectProfilingHandlers(context, profiling)).containsOnly(loggingProfilingHandler);
        verifyAll();
    }

    @Profiling
    public void testMethod(String arg1, boolean arg2)
    {
        // nothing to do here, only for test-purposes
    }

    @Test
    public void testConstructor()
    {
        ProfilerUtils utils = new ProfilerUtils()
        {
        };
        assertThat(utils).isNotNull();
    }
}