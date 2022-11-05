package test;

import framework.annotations.*;
import framework.http.response.JsonResponse;
import test.dependencies.Service2;

import java.util.Map;

@Controller
public class Controller2 {

    @Autowired(verbose = false)
    @Qualifier("service2")
    private Service2 service2;

    public Controller2() {
    }

    @GET
    @Path(path = "/controller2")
    public JsonResponse controllerGet(){
        Map<String, String> controllerMap = service2.returnMessage();
        controllerMap.put("GET", "Controller2");

        return new JsonResponse(controllerMap);
    }
}
