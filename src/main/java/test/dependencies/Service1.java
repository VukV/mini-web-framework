package test.dependencies;

import framework.annotations.Autowired;
import framework.annotations.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Service1 {

    @Autowired(verbose = true)
    private Component1 component1;

    public Service1() {
    }

    public Map<String, String> returnMessage(){
        Map<String, String> mapMessage = new HashMap<>();
        mapMessage.put("message", "Hello from Service1");
        mapMessage.put("componentMessage", component1.componentMessage());
        return mapMessage;
    }
}
