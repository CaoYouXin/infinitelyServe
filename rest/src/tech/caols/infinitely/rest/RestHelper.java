package tech.caols.infinitely.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.server.SimpleServer;
import tech.caols.infinitely.server.handlers.ProxyHandler;

import java.lang.reflect.Method;

public class RestHelper {

    private static final Logger logger = LogManager.getLogger(RestHelper.class);

    private final SimpleServer simpleServer;

    public RestHelper(SimpleServer simpleServer) {
        this.simpleServer = simpleServer;
    }

    public RestHelper addRestObject(Object object) {
        Rest rest = object.getClass().getDeclaredAnnotation(Rest.class);

        if (null == rest) {
            throw new RuntimeException("not a rest object.");
        }

        logger.info("parsing @ " + object.getClass().getName());
        for (Method method : object.getClass().getMethods()) {
            RestAPI restAPI = method.getDeclaredAnnotation(RestAPI.class);

            if (restAPI != null) {
                logger.info("serving @ " + restAPI.url());
                this.simpleServer.registerHandler(restAPI.url(), new ProxyHandler(
                    new RestHandler(object, method, restAPI)
                ));
            }
        }
        logger.info("parsing finished @ " + object.getClass().getName());

        return this;
    }

}
