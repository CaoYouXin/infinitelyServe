package tech.caols.infinitely.hanlders;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;

import java.io.IOException;

public class PreHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(PreHandler.class);

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        logger.info("pre 001 handle");

        HttpUtils.response(httpResponse, JsonRes.SuccessJsonRes);
    }
}
