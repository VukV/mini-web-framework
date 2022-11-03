package framework.engine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
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
    private Set<Class<?>> containers;

    private ClassScanner classScanner;

    public DependencyInjectionEngine() {
        dependencyContainer = new HashMap<>();
        services = new HashSet<>();
        containers = new HashSet<>();
        classScanner = new ClassScanner();

        arrangeClasses();
        injectFields();
    }

    private void arrangeClasses(){
        //TODO
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
