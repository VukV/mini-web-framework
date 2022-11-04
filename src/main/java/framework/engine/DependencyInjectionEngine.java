package framework.engine;

import framework.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DependencyInjectionEngine {


    /**
     * DependencyContainer keeps information about interface implementations.
     * String - unique qualifier name
     * Class - interface implementation
     */
    private Map<String, Class<?>> dependencyContainer;
    private Set<Object> serviceInstances;

    private Set<Class<?>> services;
    private Set<Class<?>> components;
    private Set<Class<?>> controllers;

    private ClassScanner classScanner;

    public DependencyInjectionEngine() {
        dependencyContainer = new HashMap<>();
        serviceInstances = new HashSet<>();

        services = new HashSet<>();
        components = new HashSet<>();
        controllers = new HashSet<>();

        classScanner = new ClassScanner();
    }

    public void inject() throws Exception {
        arrangeClasses();
        instantiateControllers();
        controllerInjection();
    }

    private void arrangeClasses() throws Exception {
        Set<Class<?>> allClasses = classScanner.scanAllClasses();

        boolean isBean;
        for(Class<?> currentClass: allClasses){
            isBean = false;
            if(!currentClass.isInterface() && !currentClass.isEnum() && !currentClass.isAnnotation()){
                if(currentClass.isAnnotationPresent(Controller.class)){
                    controllers.add(currentClass);
                }
                else if(currentClass.isAnnotationPresent(Service.class)){
                    services.add(currentClass);
                    isBean = true;
                }
                else if(currentClass.isAnnotationPresent(Component.class)){
                    components.add(currentClass);
                    isBean = true;
                }
                else if(currentClass.isAnnotationPresent(Bean.class)){
                    Scope currentClassScope = currentClass.getAnnotation(Bean.class).scope();
                    isBean = true;
                    if (currentClassScope == Scope.SINGLETON){
                        services.add(currentClass);
                    }
                    else if(currentClassScope == Scope.PROTOTYPE){
                        components.add(currentClass);
                    }
                }

                if(currentClass.isAnnotationPresent(Qualifier.class)){
                    if(isBean){
                        if(dependencyContainer.putIfAbsent(currentClass.getAnnotation(Qualifier.class).value(), currentClass) != null){
                            throw new RuntimeException("Qualifier value must be unique!");
                        }
                    }
                    else {
                        throw new RuntimeException("Class with Qualifier must also be a Bean class!");
                    }
                }
            }
        }
    }

    private void instantiateControllers() throws Exception {
        RouteRegistrationEngine.getInstance().instantiateControllers(controllers);
    }

    private void controllerInjection() throws Exception{
        for(Map.Entry<Class<?>, Object> entry: RouteRegistrationEngine.getInstance().getControllerInstances().entrySet()){
            injectFields(entry.getKey(), entry.getValue());
        }
    }

    private void injectFields(Class<?> controllerClass, Object controllerInstance) throws Exception {
        for(Field field: controllerClass.getDeclaredFields()){
            if(!field.isAnnotationPresent(Autowired.class)){
                continue;
            }

            Class<?> fieldClass = field.getType();
            if(services.contains(fieldClass)){
                injectService(controllerInstance, field, fieldClass);
            }
            else if(components.contains(fieldClass)){
                injectComponent(controllerInstance, field, fieldClass);
            }
            else if(field.isAnnotationPresent(Qualifier.class)) {
                Class<?> implementationClass = dependencyContainer.get(field.getAnnotation(Qualifier.class).value());
                if(implementationClass != null){
                    if(services.contains(implementationClass)){
                        injectService(controllerInstance, field, implementationClass);
                    }
                    else if(components.contains(implementationClass)){
                        injectComponent(controllerInstance, field, implementationClass);
                    }
                    else {
                        throw new RuntimeException("There is no implementation with given Qualifier!");
                    }
                }
                else {
                    throw new RuntimeException("There is no implementation with given Qualifier!");
                }
            }
            else {
                throw new RuntimeException("No class found!");
            }
        }
    }

    /**
     * Method that injects services.
     * @param instanceToInjectInto - Object whose field is being injected.
     * @param fieldForInjection - Field that is being injected, used to check verbose parameter.
     * @param serviceClass - Class of the field that is being injected, used to find/instantiate services.
     */
    private void injectService(Object instanceToInjectInto, Field fieldForInjection, Class<?> serviceClass) throws Exception{
        Object serviceInstance = null;
        boolean alreadyInstantiated = false;

        for(Object si: serviceInstances){
            if(serviceClass.isInstance(si)){
                serviceInstance = si;
                alreadyInstantiated = true;
                break;
            }
        }

        if(!alreadyInstantiated){
            Constructor<?> serviceConstructor = serviceClass.getConstructor();
            serviceConstructor.setAccessible(true);
            serviceInstance = serviceConstructor.newInstance();

            if(serviceClass.getDeclaredFields().length > 0){
                injectFields(serviceClass, serviceInstance);
            }

            if(fieldForInjection.getAnnotation(Autowired.class).verbose()){
                printVerbose(fieldForInjection, serviceInstance);
            }

            serviceInstances.add(serviceInstance);
        }

        fieldForInjection.setAccessible(true);
        fieldForInjection.set(instanceToInjectInto, serviceInstance);
    }

    /**
     * Method that injects components.
     * @param instanceToInjectInto - Object whose field is being injected.
     * @param fieldForInjection - Field that is being injected, used to check verbose parameter.
     * @param componentClass - Class of the component that is being injected, used to instantiate components.
     */
    private void injectComponent(Object instanceToInjectInto, Field fieldForInjection, Class<?> componentClass) throws Exception{
        Constructor<?> componentConstructor = componentClass.getConstructor();
        componentConstructor.setAccessible(true);
        Object componentInstance = componentConstructor.newInstance();

        if(componentClass.getDeclaredFields().length > 0){
            injectFields(componentClass, componentInstance);
        }

        if(fieldForInjection.getAnnotation(Autowired.class).verbose()){
            printVerbose(fieldForInjection, componentInstance);
        }

        fieldForInjection.setAccessible(true);
        fieldForInjection.set(instanceToInjectInto, componentInstance);
    }

    private void printVerbose(Field field, Object objectInstance){
        System.out.println("Initialized " + field.getType().getName() + " " + field.getName() + " in "
                + field.getDeclaringClass().getName() + " on "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"))
                + " with " + objectInstance.hashCode());
    }
}