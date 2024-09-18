# Using Java's Map as a Dependency Injection Tool

This project demonstrates the use of Java's `Map`as a lightweight
alternative for Dependency Injection (DI). The usage is comparable
to Spring's Java configuration when DI is the only requirement.

Of course, Spring is a comprehensive framework offering a wealth of
functionality beyond DI.

## Description

With Java 8, the `Map` interface introduced several new methods,
one of which is particularly useful for implementing a simple DI
solution. This project showcases that approach in
[MapAppContext.java](./src/main/java/mt/articles/mapasdi/MapAppContext.java).
For comparison, the same functionality is implemented using Spring in
[SpringAppContext.java](./src/main/java/mt/articles/mapasdi/SpringAppContext.java).

The starting point is [Main.java](./src/main/java/mt/articles/mapasdi/Main.java),
where both contexts are loaded 1000 times as a basic benchmark. The results
of running the code 1000 times are as follows:

```
Spring time:  2275449800 ns, res: ServiceBean(nativeEncoding=Cp1250, dao=DaoBean(fileEncoding=UTF-8), manager=ManagerBean(dao=DaoBean(fileEncoding=UTF-8)))
Map time:       11775400 ns, res: ServiceBean(nativeEncoding=Cp1250, dao=DaoBean(fileEncoding=UTF-8), manager=ManagerBean(dao=DaoBean(fileEncoding=UTF-8)))
```

Both DI approaches successfully initialize the same code. However,
while Spring offers numerous features, bootstrap speed is not one
of its strengths.



## How does it work?


The basic idea of using map as Dependency Injection (DI) tools comes from two needs of DI:

    1. "Outside initialization"
    2. "Declarative initialization"


"Outside initialization" essentially states that code used to initialize app is
written in different files than business code being initialized. Non trivial app
can have as much code in initialization as in their business logic, and as those
two parts of source have completely separated concerns and lifecylces, it makes
no sense to mix them. Classic way to achieve this separation is by using a
Factory pattern.


"Declarative initialization" is a need of initialization code to be as much
resilient of ordering as it is possible. If there is a dependency of
object A on other object B, then in order for A to be work, B has to be
initialized first. This is "order of needs". A needs B thus B has to be in
workable state first. If in naive source code A is initialized or used first,
an error will happen, either at compile or at runtime. Thus usage of DI frameworks.
DI frameworks are used to "figure out" proper initialization order. Thus making
initialization code much more resilient to later changes.


With little thought, both of those concerns can be met by simple Java Map. Since Java
8 Map interface has {@link Map#computeIfAbsent(Object, java.util.function.Function)}
method. This method can be used to fetch allready initialized objects or to init a
new instance if one is not found. Within Context each "bean" has an associated factory method,
just as in Spring's Java Config. However actual bean initialization happens within
map as part <code>computeIfAbsent</code>'s lambda. As looking up for a bean within map
triggers initialization of one, the actual order of initialization will always be aligned
to the "order of needs". It is declarative.


This example will load an 'App' consisting of service, manager and dao and few
configuration variables. Same App context is implemented twice, using Map and
using Java Config. The examples mostly map line to line. The one based on Map has
few constraints which Spring lacks. Mostly that bean dependencies need to be
pulled into a method by invoking appropriate factory methods first. Fetch of
dependencies must happen before computeIfAbsent is invoked and dependencies have to
be passed into initializing lambda as closure. Factory methods CAN NOT be invoked
within lambda as that would trigger undefined behavior within Ma. At end of
the day each factory method is reading and/or writing into the same map.
In Spring config dependencies can be passed as method parameters.


Demonstrated here too is handling of close. Sometimes resources have to be closed,
and with map config we have to do it manually. In Spring {@code Bean} has destroyMethod
property and most of time that is sufficient.


Also environment entries have to be parsed manually and put into scope. This is
demonstrated by using {@link System#getProperties()}.


But overall code is mostly the same. If the only thing needed is DI than this map based
DI works just fine. Additional plus is no runtime dependencies and much much faster
to execute.



## Build Instructions

This project is a simple Maven-based Java 8 application.
To build the project, simply run `mvn package`.
