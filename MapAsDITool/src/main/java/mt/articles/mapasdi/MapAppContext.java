package mt.articles.mapasdi;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The basic idea of using map as Dependency Injection (DI) tools comes from two needs of DI:
 *
 *   1. "Outside initialization"
 *   2. "Declarative initialization"
 *
 * "Outside initialization" essentially states that code used to initialize app is
 * written in different files than business code being initialized. Non trivial app
 * can have as much code in initialization as in their business logic, and as those
 * two parts of source have completely separated concerns and lifecylces, it makes
 * no sense to mix them. Classic way to achieve this separation is by using a
 * Factory pattern.
 *
 * "Declarative initialization" is a need of initialization code to be as much
 * resilient of ordering as it is possible. If there is a dependency of
 * object A on other object B, then in order for A to be work, B has to be
 * initialized first. This is "order of needs". A needs B thus B has to be in
 * workable state first. If in naive source code A is initialized or used first,
 * an error will happen, either in compile or in runtime. Thus usage of DI frameworks.
 * DI frameworks are used to "figure out" proper initialization order. Thus making
 * initialization code much more resilient to later changes.
 *
 * With little thought, both of those concerns can be met by simple Java Map. Since Java
 * 8 Map interface has {@link Map#computeIfAbsent(Object, java.util.function.Function)}
 * method. This method can be used to fetch allready initialized objects or to init a
 * new instance if one is not found. Each "bean" has an associated factory method,
 * just as in Spring's Java Config. However actual bean initialization happens within
 * map as part <code>computeIfAbsent</code>'s lambda. As looking up for a bean within map
 * triggers initialization of one, the actual order is aligned to "order of needs" -
 * it is a declarative.
 *
 * The following example will load an 'App' consisting of service, manager and dao
 * and few configuration variables. Same example is reimplemented in {@link SpringAppContext}
 * using Java Config. The examples mostly map line to line. The one based on map has
 * few constraints which Spring lacks. Mostly that bean dependencies need to be
 * pulled into a method by invoking dependencies factory methods. Also this fetch of
 * dependencies must happen before computeIfAbsent is invoked and have to be passed into
 * initializing lambda as closure. Factory methods CAN NOT be invoked within lambda as
 * that would trigger undefined behavior within HashMap. At end of the day each factory
 * method is getting or writing into the same map. In Spring config dependencies can be
 * passed as method parameters.
 *
 * Demonstrated here too is handling of close. Sometimes resources have to be closed,
 * and with map config we have to do it manually. In Spring {@code Bean} has destroyMethod
 * property.
 *
 * Also environment entries have to be parsed manually and put into scope. Here this is
 * demonstrated by using {@link System#getProperties()}.
 *
 * But overall code is mostly the same. If the only thing needed is DI than this map based
 * DI works just fine. Additional plus is no runtime dependencies and much much faster
 * to execute.
 */
public class MapAppContext implements AutoCloseable {

    final ConcurrentHashMap<String, Object> context = new ConcurrentHashMap<>();


    Properties environment() {
        Object res = context.computeIfAbsent("environment", t -> System.getProperties());
        return (Properties) res;
    }


    String fileEncoding() {
        Properties env = environment();
        Object res = context.computeIfAbsent("fileEncoding", t -> env.getOrDefault("file.encoding", "UTF-8"));
        return (String) res;
    }


    String nativeEncoding() {
        String defaultVal = fileEncoding();
        Properties env = environment();
        Object res = context.computeIfAbsent("nativeEncoding", t -> env.getOrDefault("native.encoding", defaultVal));
        return (String) res;
    }


    ServiceBean service() {
        String val = nativeEncoding();
        DaoBean dao = dao();
        ManagerBean manager = manager();
        Object res = context.computeIfAbsent("service", t -> new ServiceBean(val, dao, manager));
        return (ServiceBean) res;
    }


    ManagerBean manager() {
        DaoBean dao = dao();
        Object res = context.computeIfAbsent("manager", t -> new ManagerBean(dao));
        return (ManagerBean) res;
    }


    DaoBean dao() {
        String enconding = fileEncoding();
        Object res = context.computeIfAbsent("dao", t -> new DaoBean(enconding));
        return (DaoBean) res;
    }


    @Override
    public void close() throws Exception {
        for (Entry<String, Object> e: context.entrySet()) {
            if (e instanceof AutoCloseable) {
                AutoCloseable ac = (AutoCloseable) e;
                ac.close();
            }
        }
    }
}

