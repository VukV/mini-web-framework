package framework.engine.routes;

import framework.annotations.GET;
import framework.annotations.POST;
import framework.annotations.Path;
import framework.http.request.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RouteRegistrationEngine {

    private static RouteRegistrationEngine instance;

    private Map<Class<?>, Object> controllerInstances;
    private Map<String, RouteMethod> routeMethods;

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
        routeMethods = new HashMap<>();
    }

    public void instantiateControllers(Set<Class<?>> controllers) throws Exception {
        for(Class<?> controllerClass: controllers){
            controllerInstances.put(controllerClass, controllerClass.getConstructor().newInstance());
        }
    }

    public Map<Class<?>, Object> getControllerInstances() {
        return controllerInstances;
    }

    public void registerRoutes(){
        for(Map.Entry<Class<?>, Object> entry: controllerInstances.entrySet()){
            Object controllerInstance = entry.getValue();

            for(Method method: entry.getKey().getDeclaredMethods()){
                if(method.isAnnotationPresent(POST.class) && method.isAnnotationPresent(GET.class)){
                    throw new RuntimeException("Method can't be GET and POST at the same time!");
                }

                if(method.isAnnotationPresent(POST.class) || method.isAnnotationPresent(GET.class)){
                    if(checkPath(method) && checkArguments(method)){
                        mapPathMethod(method, controllerInstance);
                    }
                }
            }
        }
    }

    private boolean checkPath(Method method){
        if (method.isAnnotationPresent(Path.class)){
            return true;
        }
        else {
            throw new RuntimeException("Path must be specified!");
        }
    }

    private boolean checkArguments(Method method){
        if(method.getParameterTypes().length > 1){
            throw new RuntimeException("Method can't have more than one argument!");
        }
        else if(method.getParameterTypes().length == 1 && method.getParameterTypes()[0] != Request.class){
            throw new RuntimeException("Only valid method argument is Request!");
        }
        return true;
    }

    private void mapPathMethod(Method method, Object controllerInstance){
        RouteMethod routeMethod = new RouteMethod(method, controllerInstance);
        String path = method.getAnnotation(Path.class).path();

        if(routeMethods.putIfAbsent(path, routeMethod) != null){
            throw new RuntimeException("There can't be two methods with the same path!");
        }
    }

    public void handleRequest(Request request){
        //TODO
    }
}
