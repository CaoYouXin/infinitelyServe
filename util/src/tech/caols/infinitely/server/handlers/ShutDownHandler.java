package tech.caols.infinitely.server.handlers;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.ShutDownConfig;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.SimpleServer;

import java.io.IOException;

public class ShutDownHandler implements HttpRequestHandler {

    private SimpleServer server;

    public ShutDownHandler(SimpleServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {

        if (!HttpUtils.isGet(httpRequest)) {
            throw new MethodNotSupportedException(HttpUtils.getMethod(httpRequest) + " method not supported");
        }

        String decodedUrl = HttpUtils.getDecodedUrl(httpRequest);
        int indexOf = decodedUrl.indexOf("?token=");
        if (-1 != indexOf) {
            String token = decodedUrl.substring(indexOf + "?token=".length());

            ConfigUtil util = new ConfigUtil();
            ShutDownConfig config = util.getConfigFromFile(util.getRootFileName() + ".log", ShutDownConfig.class);

            if (token.equals(config.getToken())) {
                this.server.shutdown();
            }
        } else {
            throw new RuntimeException("To shutdown, one must follow the correct format of url [/shutdown.cmd?token=TOKEN].");
        }
    }

}
