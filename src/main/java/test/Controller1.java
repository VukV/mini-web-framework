package test;

import framework.annotations.*;
import framework.http.request.Request;
import framework.http.response.JsonResponse;
import test.dependencies.Service1;
import test.dependencies.Service2;

import java.util.Map;

@Controller
public class Controller1 {

    @Autowired(verbose = true)
    private Service1 service1;

    @Autowired(verbose = false)
    @Qualifier("service2")
    private Service2 service2;

    public Controller1() {
    }

    @GET
    @Path(path = "/controller1")
    public JsonResponse controllerGet(){
        Map<String, String> controllerMap = service1.returnMessage();
        controllerMap.put("GET", "Controller1");

        return new JsonResponse(controllerMap);
    }

    @POST
    @Path(path = "/controller1")
    public JsonResponse controllerGet(Request request){
        System.out.println(request.getParameters());

        Map<String, String> controllerMap = service2.returnMessage();
        controllerMap.put("GET", "Controller1");

        return new JsonResponse(controllerMap);
    }
}
