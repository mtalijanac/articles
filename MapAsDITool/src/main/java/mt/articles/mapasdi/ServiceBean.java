package mt.articles.mapasdi;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data @AllArgsConstructor
class DaoBean implements AutoCloseable {
    String fileEncoding;

    public void close() throws Exception {
        // System.out.println("Resources closed..");
    }
}


@Data @AllArgsConstructor
class ManagerBean {
    DaoBean dao;
}


@Data @AllArgsConstructor
class ServiceBean {
    String nativeEncoding;
    DaoBean dao;
    ManagerBean manager;

    public String runTheApp() {
        return toString();
    }
}