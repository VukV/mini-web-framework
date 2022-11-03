package framework.engine;

import framework.annotations.*;
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
    private Set<Class<?>> services;
    private Set<Class<?>> components;
    private Set<Class<?>> controllers;

    private ClassScanner classScanner;
    private RouteRegistrationEngine routeRegistrationEngine;

    public DependencyInjectionEngine() throws Exception {
        dependencyContainer = new HashMap<>();
        services = new HashSet<>();
        components = new HashSet<>();
        controllers = new HashSet<>();

        classScanner = new ClassScanner();
        routeRegistrationEngine = new RouteRegistrationEngine();
    }

    public void inject() throws Exception {
        arrangeClasses();
        //TODO RouteRegistration add controllers
        injectFields();
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

    private void injectFields(){
        //TODO
    }

    private void printVerbose(Field field, Object objectInstance){
        System.out.println("Initialized " + field.getType().getName() + " " + field.getName() + " in "
                + field.getDeclaringClass().getName() + " on "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"))
                + " with " + objectInstance.hashCode());
    }
}
