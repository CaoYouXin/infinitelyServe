package tech.caols.infinitely.handlers;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.server.HttpUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GetServerConfigHandler implements HttpRequestHandler {

    private final String serverRoot;

    public GetServerConfigHandler(String serverRoot) {
        this.serverRoot = serverRoot;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!HttpUtils.isGet(httpRequest)) {
            throw new MethodNotSupportedException("not a GET method, but a " + HttpUtils.getMethod(httpRequest));
        }

        Map<String, String> parameterMap = HttpUtils.getParameterMap(httpRequest);
        String serverName = parameterMap.get("serverName");

        if (serverName == null) {
            throw new RuntimeException("request format error, need do and at");
        }

        String serverFileName = serverName.substring(0, serverName.lastIndexOf("_jar"));
        File file = new File(this.serverRoot + serverName, serverFileName + ".json");

        HttpUtils.response(httpContext, SimpleUtils.getFileWithUtil(file));
    }

}
