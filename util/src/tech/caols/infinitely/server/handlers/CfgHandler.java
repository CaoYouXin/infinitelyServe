package tech.caols.infinitely.server.handlers;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.config.PrePostConfig;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;

import java.io.IOException;
import java.util.Map;

public class CfgHandler implements HttpRequestHandler {

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {

        if (!HttpUtils.isGet(httpRequest)) {
            throw new MethodNotSupportedException(HttpUtils.getMethod(httpRequest) + " method not supported");
        }

        Map<String, String> parameterMap = HttpUtils.getParameterMap(httpRequest);
        String loc =  parameterMap.get("loc"),
                port =  parameterMap.get("port"),
                url =  parameterMap.get("url"),
                needBody = parameterMap.get("needBody"),
                parameters = parameterMap.get("parameters");

        if (loc != null && port != null && url != null) {
            String decodedUrl = HttpUtils.getDecodedUrl(httpRequest);
            String type = decodedUrl.substring(1, decodedUrl.indexOf(".cfg?"));
            PrePostConfig.get().setHost(url, type, new HttpHost(loc, Integer.parseInt(port)));
            if (needBody != null) {
                PrePostConfig.get().setNeedBody(url, type, Boolean.parseBoolean(needBody));
            }
            if (parameters != null) {
                PrePostConfig.get().setParameters(url, type, HttpUtils.getListParameter(parameters));
            }

            HttpUtils.response(httpResponse, new JsonRes(Constants.CODE_VALID));
            System.out.println(PrePostConfig.get());
        } else {
            throw new RuntimeException("wrong config url pattern.");
        }
    }

}
