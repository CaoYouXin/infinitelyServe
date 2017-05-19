package tech.caols.infinitely.rest;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.server.HttpUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RestHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(RestHandler.class);

    private final Object object;
    private final Method method;
    private final RestAPI rest;

    public RestHandler(Object object, Method method, RestAPI rest) {
        this.object = object;
        this.method = method;
        this.rest = rest;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

        if (!this.rest.target().name().equals(HttpUtils.getMethod(request))) {
            throw new MethodNotSupportedException("not a " + this.rest.target().name() + ". but a " + HttpUtils.getMethod(request));
        }

        Object ret = null;
        try {
            ret = this.method.invoke(object, request, context);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.catching(e);
        }

        if (ret != null) {
            HttpUtils.response(context, ret);
        }
    }

}
