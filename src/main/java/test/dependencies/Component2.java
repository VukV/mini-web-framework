package test.dependencies;

import framework.annotations.Component;

@Component
public class Component2 {

    public Component2() {
    }

    public String componentMessage(){
        return "Component 2";
    }
}
