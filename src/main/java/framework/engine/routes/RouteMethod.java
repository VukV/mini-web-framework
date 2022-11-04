package framework.engine.routes;

import java.lang.reflect.Method;

public class RouteMethod {

    private Method method;
    private Object controllerInstance;

    public RouteMethod(Method method, Object controllerInstance) {
        this.method = method;
        this.controllerInstance = controllerInstance;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getControllerInstance() {
        return controllerInstance;
    }

    public void setControllerInstance(Object controllerInstance) {
        this.controllerInstance = controllerInstance;
    }
}
