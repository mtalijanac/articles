# Using Java's Map as Dependency Injection tool


This project is demonstration of usage Java's Map as Dependency Injection tool.
Usage is comparable to Spring's Java config as long as DI is the only requirement.

Spring is, of course, proper framework with lots of usable code outside DI.


## Description

Since Java 8 Map interface has additional methods of which one is very useful
for as stand in implementation of DI. Demonstration of this usage is implemented in
[MapAppContext.java](./src/main/java/mt/articles/mapasdi/MappAppContext.java) and
for comparison project also contains implementation of same code but using Spring in
[SpringAppContext.java](./src/main/java/mt/articles/mapasdi/SpringAppContext.java)

Starting point is in [Main.java](./src/main/java/mt/articles/mapasdi/Main.java)
where both contextes are loaded 1000 times as rudimentary benchmark. Output of
running code 1000 times:

    Spring time:   2275449800 ns, res: ServiceBean(nativeEncoding=Cp1250, dao=DaoBean(fileEncoding=UTF-8), manager=ManagerBean(dao=DaoBean(fileEncoding=UTF-8)))
    Map time:        11775400 ns, res: ServiceBean(nativeEncoding=Cp1250, dao=DaoBean(fileEncoding=UTF-8), manager=ManagerBean(dao=DaoBean(fileEncoding=UTF-8)))


Essentially both context implementations have successfully initialized the same code.
And while Spring has many features, bootstrap speed is not the one to brag about.


## Build it yourself

This project is a plain Maven jar project on top of Java 8.
To build project 'mvn package' should be sufficient.

