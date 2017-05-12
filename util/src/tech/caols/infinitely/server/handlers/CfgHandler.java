package tech.caols.infinitely.server.handlers;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import tech.caols.infinitely.config.PrePostConfig;
import tech.caols.infinitely.server.HttpUtils;

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
                url =  parameterMap.get("url");

        if (loc != null && port != null && url != null) {
            String decodedUrl = HttpUtils.getDecodedUrl(httpRequest);
            PrePostConfig.get().setHost(url, decodedUrl.substring(1, decodedUrl.indexOf(".cfg?")),
                    new HttpHost(loc, Integer.parseInt(port)));

            System.out.println(PrePostConfig.get());
        } else {
            throw new RuntimeException("wrong config url pattern.");
        }
    }

}
