package tech.caols.infinitely.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.pool.BasicPoolEntry;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.config.PostConfig;
import tech.caols.infinitely.config.PreConfig;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.server.SimplePool;

import java.io.IOException;
import java.util.*;

public class ProxyHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(ProxyHandler.class);

    private HttpRequestHandler handler;

    private HttpProcessor httpproc = HttpProcessorBuilder.create()
            .add(new RequestContent())
            .add(new RequestTargetHost())
            .add(new RequestConnControl())
            .add(new RequestUserAgent("Test/1.1"))
            .add(new RequestExpectContinue(true)).build();
    private HttpRequestExecutor httpExecutor = new HttpRequestExecutor();

    public ProxyHandler(HttpRequestHandler handler) {
        this.handler = handler;
    }

    private void setCORS(HttpRequest httpRequest, String reqHeaderName, HttpResponse httpResponse, String resHeaderName) {
        Header[] headers = httpRequest.getHeaders(reqHeaderName);
        logger.info(reqHeaderName + " : " + Arrays.toString(headers));

        if (headers.length > 0) {
            for (Header header : headers) {
                httpResponse.setHeader(resHeaderName, header.getValue());
            }
        }
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {


        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        setCORS(httpRequest, "Origin",
                httpResponse, "Access-Control-Allow-Origin");

        setCORS(httpRequest, "Access-Control-Allow-Method",
                httpResponse, "Access-Control-Request-Methods");

        setCORS(httpRequest, "Access-Control-Request-Headers",
                httpResponse, "Access-Control-Allow-Headers");

        setCORS(httpRequest, "Access-Control-Allow-Credentials",
                httpResponse, "Access-Control-Allow-Credentials");


        if (HttpUtils.isOptions(httpRequest)) {

            httpResponse.setStatusCode(HttpStatus.SC_OK);
            return;

        } else if (HttpUtils.isInvalidMethod(httpRequest)) {
            throw new MethodNotSupportedException(HttpUtils.getMethod(httpRequest) + " method not supported");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String url = HttpUtils.getSimpleDecodedUrl(httpRequest);
        Map<String, String> parameterMap = HttpUtils.getParameterMap(httpRequest);

        for (PreConfig config : PreConfig.match(url)) {
            BasicPoolEntry connEntry = SimplePool.get().getConn(config.getHost());
            HttpClientConnection conn = connEntry.getConnection();
            HttpCoreContext coreContext = HttpCoreContext.create();
            coreContext.setTargetHost(config.getHost());

            Map map = new HashMap();
            if (config.getParameters() != null && config.getParameters().size() > 0) {
                Map parameters = new HashMap();
                for (String param : config.getParameters()) {
                    parameters.put(param, parameterMap.get(param));
                }
                map.put("parameters", parameters);
            }

            if (config.isNeedBody()) {
                map.put("body", HttpUtils.getBodyAsString(httpRequest));
            }

            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", config.getUrl());
            request.setEntity(new StringEntity(objectMapper.writeValueAsString(map), ContentType.APPLICATION_JSON));
            logger.info(">> Request URI: " + request.getRequestLine().getUri());

            this.httpExecutor.preProcess(request, this.httpproc, coreContext);
            HttpResponse response = this.httpExecutor.execute(request, conn, coreContext);
            this.httpExecutor.postProcess(response, this.httpproc, coreContext);

            logger.info("<< Response: " + response.getStatusLine());
            String preRet = EntityUtils.toString(response.getEntity());
            logger.info(preRet);
            logger.info("==============");
            SimplePool.get().release(connEntry);

            HashMap preRetObject = objectMapper.readValue(preRet, HashMap.class);
            if (preRetObject.get(Constants.CODE).equals(Constants.INVALID)) {

                HttpUtils.response(httpResponse, JsonRes.getFailJsonRes("one of the pre processors commands returning a fail."));
                return;
            } else {
                Map sets = (Map) preRetObject.get(Constants.OUT_SET);
                if (null != sets) {
                    for (Object key : sets.keySet()) {
                        httpContext.setAttribute((String) key, sets.get(key));
                    }
                }

                List removes = (List) preRetObject.get(Constants.OUT_REMOVE);
                if (null != removes) {
                    for (Object removeKey : removes) {
                        httpContext.removeAttribute((String) removeKey);
                    }
                }
            }
        }

        this.handler.handle(httpRequest, httpResponse, httpContext);

        for (PostConfig config : PostConfig.match(url)) {
            BasicPoolEntry connEntry = SimplePool.get().getConn(config.getHost());
            HttpClientConnection conn = connEntry.getConnection();
            HttpCoreContext coreContext = HttpCoreContext.create();
            coreContext.setTargetHost(config.getHost());

            Map reqMap = new HashMap();
            if (config.getParameters() != null && config.getParameters().size() > 0) {
                Map parameters = new HashMap();
                for (String param : config.getParameters()) {
                    parameters.put(param, parameterMap.get(param));
                }
                reqMap.put("parameters", parameters);
            }

            if (config.isNeedBody()) {
                reqMap.put("body", HttpUtils.getBodyAsString(httpRequest));
            }

            if (config.isNeedRetObj()) {
                Object retObject = httpContext.getAttribute(Constants.RET_OBJECT);
                if (null != retObject) {
                    reqMap.put("ret", objectMapper.writeValueAsString(retObject));
                }
            }

            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", config.getUrl());
            request.setEntity(new StringEntity(objectMapper.writeValueAsString(reqMap), ContentType.APPLICATION_JSON));
            logger.info(">> Request URI: " + request.getRequestLine().getUri());

            this.httpExecutor.preProcess(request, this.httpproc, coreContext);
            HttpResponse response = this.httpExecutor.execute(request, conn, coreContext);
            this.httpExecutor.postProcess(response, this.httpproc, coreContext);

            logger.info("<< Response: " + response.getStatusLine());
            String postRet = EntityUtils.toString(response.getEntity());
            logger.info(postRet);
            logger.info("==============");
            SimplePool.get().release(connEntry);

            HashMap postRetObject = objectMapper.readValue(postRet, HashMap.class);
            if (postRetObject.get(Constants.CODE).equals(Constants.INVALID)) {

                HttpUtils.response(httpResponse, JsonRes.getFailJsonRes("one of the post processors commands returning a fail."));
                return;
            } else if (postRetObject.get(Constants.CODE).equals(Constants.REPLACE_VALID)) {

                httpContext.setAttribute(Constants.RET_OBJECT_STRING, postRetObject.get(Constants.BODY));
            }
        }

        Object retObjectString = httpContext.getAttribute(Constants.RET_OBJECT_STRING);
        if (null != retObjectString) {

            HttpUtils.response(httpResponse, (String) retObjectString);
        } else {

            Object retObject = httpContext.getAttribute(Constants.RET_OBJECT);
            if (null != retObject) {
                HttpUtils.response(httpResponse, new JsonRes<>(Constants.CODE_VALID, retObject));
            }
        }

    }

}
