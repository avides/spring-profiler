spring-profiler
============

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.avides.spring/spring-profiler/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.avides.spring/spring-profiler)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/49fe00fd4ec843b6ac21b2d3996f2de9)](https://www.codacy.com/app/developer_6/spring-profiler)
[![Coverage Status](https://coveralls.io/repos/github/avides/spring-profiler/badge.svg?branch=master)](https://coveralls.io/github/avides/spring-profiler?branch=master)
[![Build Status](https://travis-ci.org/avides/spring-profiler.svg?branch=master)](https://travis-ci.org/avides/spring-profiler)

#### Maven
```xml
<dependency>
    <groupId>com.avides.spring</groupId>
    <artifactId>spring-profiler</artifactId>
    <version>0.0.8-RELEASE</version>
</dependency>
```
#### Simple example
```java
Profiler profiler = Profiler.startProfiling("anyIdentifier");
Thread.sleep(100);
// or any other code you want to profile
profiler.stop();
```
This will give you a log-message (debug-level) that looks similar to this:

```text
execution time for anyIdentifier: 102ms
```
After stopping the profiler, you have access to some execution details, for example the exact execution-time by calling profiler.getDurationMillis()
#### Example with explicit given ProfilingHandler and allowedMillis
```java
Profiler profiler = Profiler.startProfiling("anyIdentifier", new SysoutProfilingHandler());
Thread.sleep(100);
// or any other code you want to profile
profiler.stop(50);
```
This will give you an output to the System.out that looks similar to this:

```text
WARNING: execution time for anyIdentifier: 102ms (allowed: 50ms)
```
If you do not specify a ProfilingHandler, a LoggingProfilingHandler is used as default (see example above). If you do so and the allowed time is exceeded, the logging-level switches to warning.

Of course, you can give your own ProfilingHandler-implementation (and even several at once) that meets your needs

#### Profiling of methods simply by annotation (only supported in a spring-application)
```java
@Component
public class anyClass
{
    @Profiling
    public void anyMethod()
    {
        Thread.sleep(100);
        // or any other code you want to profile
    }
}
```
To get this working, simply add an @EnableProfiling-annotation at any of your configuration-classes (or the Application-class). After executing the method "anyMethod", it will give you a log-message (debug-level) that looks similar to this:

```text
execution time for method anyClass.anyMethod(): 102ms
```
Explicit ProfilingHandlers and allowedMillis (explained in example above) can be set via the @Profiling-annotation-attributes (which also are well documented in javadoc)
