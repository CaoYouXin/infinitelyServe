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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

            List<Object> parameters = new ArrayList<>();
            for (Class<?> clazz : this.method.getParameterTypes()) {
                String typeName = clazz.getTypeName();
                switch (typeName) {
                    case "org.apache.http.HttpRequest":
                        parameters.add(request);
                        break;
                    case "org.apache.http.HttpResponse":
                        parameters.add(response);
                        break;
                    case "org.apache.http.protocol.HttpContext":
                        parameters.add(context);
                        break;
                    case "java.util.Map":
                        parameters.add(HttpUtils.getParameterMap(request));
                        break;
                    default:
                        parameters.add(HttpUtils.getBodyAsObject(request, clazz));
                        break;
                }
            }

            switch (parameters.size()) {
                case 0:
                    ret = this.method.invoke(this.object);
                    break;
                case 1:
                    ret = this.method.invoke(this.object, parameters.get(0));
                    break;
                case 2:
                    ret = this.method.invoke(this.object, parameters.get(0), parameters.get(1));
                    break;
                case 3:
                    ret = this.method.invoke(this.object, parameters.get(0), parameters.get(1), parameters.get(2));
                    break;
                case 4:
                    ret = this.method.invoke(this.object, parameters.get(0),
                            parameters.get(1), parameters.get(2), parameters.get(3));
                    break;
                case 5:
                    ret = this.method.invoke(this.object, parameters.get(0), parameters.get(1),
                            parameters.get(2), parameters.get(3), parameters.get(4));
                    break;
                default:
                    throw new RuntimeException("始料未及. parameter count = " + parameters.size());
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.catching(e);
        }

        if (ret != null) {
            HttpUtils.response(context, ret);
        }


    }

}
