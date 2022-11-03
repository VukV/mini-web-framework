package framework.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RouteRegistrationEngine {

    private Map<Class<?>, Object> controllerInstances;

    public RouteRegistrationEngine(){
        controllerInstances = new HashMap<>();
    }

    public void setControllers(Set<Class<?>> controllers){
        //TODO map(controller class, controller object)
    }

}
