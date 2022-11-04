package framework.http.request;

import java.util.HashMap;

public class Helper {

    public static HashMap<String, String> getParametersFromRoute(String route) {
        String[] splitRoute = route.split("\\?");

        if(splitRoute.length == 1) {
            return new HashMap<String, String>();
        }

        return Helper.getParametersFromString(splitRoute[1]);
    }

    public static HashMap<String, String> getParametersFromString(String parametersString) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        String[] pairs = parametersString.split("&");
        for (String pair:pairs) {
            String[] keyPair = pair.split("=");
            parameters.put(keyPair[0], keyPair[1]);
        }

        return parameters;
    }
}
