package framework.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RouteRegistrationEngine {

    private static RouteRegistrationEngine instance;
    private Map<Class<?>, Object> controllerInstances;

    public static RouteRegistrationEngine getInstance(){
        if(instance == null){
            synchronized (RouteRegistrationEngine.class) {
                if(instance == null){
                    instance = new RouteRegistrationEngine();
                }
            }
        }
        return instance;
    }

    private RouteRegistrationEngine(){
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
