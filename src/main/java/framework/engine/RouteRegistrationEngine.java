package framework.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RouteRegistrationEngine {

    private Map<Class<?>, Object> controllerInstances;

    public RouteRegistrationEngine(){
        controllerInstances = new HashMap<>();
    }

    public void instantiateControllers(Set<Class<?>> controllers) throws Exception {
        for(Class<?> controllerClass: controllers){
            controllerInstances.put(controllerClass, controllerClass.getConstructor().newInstance());
        }
    }

    public Map<Class<?>, Object> getControllerInstances() {
        return controllerInstances;
    }
}
