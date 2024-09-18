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

## Build Instructions

This project is a simple Maven-based Java 8 application.
To build the project, simply run `mvn package`.
