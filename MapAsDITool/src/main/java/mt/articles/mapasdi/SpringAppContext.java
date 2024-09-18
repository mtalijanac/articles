package mt.articles.mapasdi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

@Configuration
public class SpringAppContext {

    @Autowired
    Environment environment;


    @Bean
    String fileEncoding() {
        String val = environment.getProperty("file.encoding", "UTF-8");
        return val;
    }


    @Bean
    String nativeEncoding() {
        String defaultVal = fileEncoding();
        String val = environment.getProperty("native.encoding", defaultVal);
        return val;
    }


    @Bean
    ServiceBean service(String nativeEncoding, DaoBean dao, ManagerBean manager) {
        ServiceBean res = new ServiceBean(nativeEncoding, dao, manager);
        return res;
    }


    @Bean
    ManagerBean manager(DaoBean dao) {
        ManagerBean res = new ManagerBean(dao);
        return res;
    }


    @Bean(destroyMethod = "close")
    DaoBean dao(String fileEncoding) {
        DaoBean res = new DaoBean(fileEncoding);
        return res;
    }
}
