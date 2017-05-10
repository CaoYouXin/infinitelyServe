package tech.caols.infinitely.server.handlers;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.ShutDownConfig;
import tech.caols.infinitely.server.SimpleServer;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;

public class ShutDownHandler implements HttpRequestHandler {

    private SimpleServer server;

    public ShutDownHandler(SimpleServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        String method = httpRequest.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
        if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
            throw new MethodNotSupportedException(method + " method not supported");
        }
        String target = httpRequest.getRequestLine().getUri();

        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
            byte[] entityContent = EntityUtils.toByteArray(entity);
            System.out.println("Incoming entity content (bytes): " + entityContent.length);
        }

        String decode = URLDecoder.decode(target, "UTF-8");
        System.out.println("shutdown : " + decode);
        int indexOf = decode.indexOf("?token=");
        if (-1 != indexOf) {
            String token = decode.substring(indexOf + "?token=".length());

            ConfigUtil util = new ConfigUtil();
            ShutDownConfig config = util.getConfig(util.getRootFileName() + ".log", ShutDownConfig.class);

            if (token.equals(config.getToken())) {
                this.server.shutdown();
            }
        }
    }

}
