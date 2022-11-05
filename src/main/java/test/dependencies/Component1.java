package test.dependencies;

import framework.annotations.Autowired;
import framework.annotations.Component;

@Component
public class Component1 {

    @Autowired(verbose = true)
    private Bean1 bean1;

    public Component1() {
    }

    public String componentMessage(){
        return "Component 1";
    }
}
