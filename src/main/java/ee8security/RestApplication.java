package ee8security;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {

//    @Override
//    public Set<Class<?>> getClasses() {
//        return Collections.singleton(AuthFilter.class);
//    }
}
