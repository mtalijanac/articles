package mt.articles.mapasdi;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) throws Exception {

        //
        // Load app using Spring as DI framework
        //
        diUsingSpring();
        long springTime = -System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            diUsingSpring();
        }
        springTime += System.nanoTime();
        String res2 = String.format("Spring time: %12s ns, res: %s", springTime, diUsingSpring());
        System.out.println(res2);



        //
        // Load app using map as DI framework
        //
        long mapTime = -System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            diUsingMap();
        }
        mapTime += System.nanoTime();
        String res = String.format("Map time:    %12d ns, res: %s", mapTime, diUsingMap());
        System.out.println(res);

    }

    static String diUsingSpring() {
        AnnotationConfigApplicationContext springAppContext = new AnnotationConfigApplicationContext(SpringAppContext.class);
        ServiceBean serviceBean = springAppContext.getBean("service", ServiceBean.class);
        String res = serviceBean.runTheApp();
        springAppContext.close();
        return res;
    }

    static String diUsingMap() throws Exception {
        MapAppContext mapAppContext = new MapAppContext();
        ServiceBean serviceBean = mapAppContext.service();
        String res = serviceBean.runTheApp();
        mapAppContext.close();
        return res;
    }
}
