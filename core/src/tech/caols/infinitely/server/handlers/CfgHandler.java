package tech.caols.infinitely.server.handlers;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.config.PostConfig;
import tech.caols.infinitely.config.PreConfig;
import tech.caols.infinitely.server.HttpUtils;

import java.io.IOException;
import java.util.Map;

public class CfgHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(CfgHandler.class);

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {

        if (!HttpUtils.isGet(httpRequest)) {
            throw new MethodNotSupportedException(HttpUtils.getMethod(httpRequest) + " method not supported");
        }

        Map<String, String> parameterMap = HttpUtils.getParameterMap(httpRequest);

        switch(parameterMap.get("type")) {
            case "PRE":
                PreConfig preConfig = HttpUtils.getBodyAsObject(httpRequest, PreConfig.class);
                switch (parameterMap.get("method")) {
                    case "register":
                        PreConfig.addConfig(preConfig);
                        break;
                    case "unregister":
                        PreConfig.removeConfig(preConfig);
                        break;
                    default:
                        String msg = "PRE unknown method : " + parameterMap.get("method");
                        logger.error(msg);
                        throw new RuntimeException(msg);
                }
                break;
            case "POST":
                PostConfig postConfig = HttpUtils.getBodyAsObject(httpRequest, PostConfig.class);
                switch (parameterMap.get("method")) {
                    case "register":
                        PostConfig.addConfig(postConfig);
                        break;
                    case "unregister":
                        PostConfig.removeConfig(postConfig);
                        break;
                    default:
                        String msg = "POST unknown method : " + parameterMap.get("method");
                        logger.error(msg);
                        throw new RuntimeException(msg);
                }
                break;
            default:
                String msg = "unknown type : " + parameterMap.get("type");
                logger.error(msg);
                throw new RuntimeException(msg);
        }

    }

}
