package tech.caols.infinitely.rest;

import tech.caols.infinitely.server.SimpleServer;
import tech.caols.infinitely.server.handlers.ProxyHandler;

import java.lang.reflect.Method;

public class RestHelper {

    private final SimpleServer simpleServer;

    public RestHelper(SimpleServer simpleServer) {
        this.simpleServer = simpleServer;
    }

    public RestHelper addRestObject(Object object) {
        Rest rest = object.getClass().getDeclaredAnnotation(Rest.class);

        if (null == rest) {
            throw new RuntimeException("not a rest object.");
        }

        for (Method method : object.getClass().getMethods()) {
            RestAPI restAPI = method.getDeclaredAnnotation(RestAPI.class);

            if (restAPI != null) {
                this.simpleServer.registerHandler(restAPI.url(), new ProxyHandler(
                    new RestHandler(object, method, restAPI)
                ));
            }
        }

        return this;
    }

}
