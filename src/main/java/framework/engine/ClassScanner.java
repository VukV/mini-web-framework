package framework.engine;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ClassScanner {

    public Set<Class<?>> scanAllClasses() throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Enumeration<URL> resources = classLoader.getResources("");
        List<File> dirs = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.toURI()));
        }
        Set<Class<?>> classes = new HashSet<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, ""));
        }
        return classes;
    }


    private List<Class<?>> findClasses(File directory, String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();

        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if(files != null){
            for (File file : files) {
                if (file.isDirectory()) {
                    if (packageName.equals("")){
                        classes.addAll(findClasses(file, file.getName()));
                    }
                    else{
                        classes.addAll(findClasses(file, packageName + "." + file.getName()));
                    }
                } else if (file.getName().endsWith(".class")) {
                    if (packageName.equals("")){
                        classes.add(Class.forName(file.getName().substring(0, file.getName().length() - 6)));
                    }
                    else{
                        classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                    }
                }
            }
        }

        return classes;
    }
}
