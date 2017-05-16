package tech.caols.infinitely.handlers;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ServerManipulationHandler implements HttpRequestHandler {

    private final String serverRoot;
    private final String uploadRoot;

    public ServerManipulationHandler(String serverRoot, String uploadRoot) {
        this.serverRoot = serverRoot;
        this.uploadRoot = uploadRoot;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!HttpUtils.isGet(httpRequest)) {
            throw new MethodNotSupportedException("not a GET method, but a " + HttpUtils.getMethod(httpRequest));
        }

        Map<String, String> parameterMap = HttpUtils.getParameterMap(httpRequest);
        String aDo = parameterMap.get("do"), at = parameterMap.get("at");
        if (aDo == null || at == null) {
            throw new RuntimeException("request format error, need do and at");
        }

        String serverFileName = at.substring(0, at.lastIndexOf("_jar"));
        String target = this.serverRoot + at + "/" + serverFileName + ".jar";
        switch (aDo) {
            case "start":
                SimpleUtils.run("java -jar " + target + " start", false);
                break;
            case "stop":
                SimpleUtils.run("java -jar " + target + " stop", false);
                break;
            case "restart":
                SimpleUtils.run("java -jar " + target + " stop", true);
                SimpleUtils.run("java -jar " + target + " start", false);
                break;
            case "deploy":
                String zipFile = this.uploadRoot + at + ".zip";
                SimpleUtils.run("unzip -qo " + zipFile + " -d " + this.serverRoot + " && rm " + zipFile, false);
                break;
            default:
                throw new RuntimeException("can not understand request " + aDo);
        }

        HttpUtils.response(httpResponse, JsonRes.SuccessJsonRes);
    }

}
