package test.dependencies;

import framework.annotations.Autowired;
import framework.annotations.Qualifier;
import framework.annotations.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Qualifier("service2")
public class Service2Implementation implements Service2{

    @Autowired(verbose = false)
    private Component2 component2;

    public Service2Implementation() {
    }

    @Override
    public Map<String, String> returnMessage() {
        Map<String, String> mapMessage = new HashMap<>();
        mapMessage.put("message", "Hello from Service2");
        mapMessage.put("componentMessage", component2.componentMessage());
        return mapMessage;
    }
}
