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

import static tech.caols.infinitely.Constants.*;

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

            Map reqMap = new HashMap();
            if (config.getParameters() != null && config.getParameters().size() > 0) {
                params(parameterMap, config, reqMap);
            }

            if (config.isNeedBody()) {
                reqMap.put("body", HttpUtils.getBodyAsString(httpRequest));
            }

            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", config.getUrl());
            request.setEntity(new StringEntity(objectMapper.writeValueAsString(reqMap), ContentType.APPLICATION_JSON));
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
            if (Integer.parseInt(preRetObject.get(CODE).toString()) == CODE_INVALID) {

                HttpUtils.response(httpResponse, JsonRes.getFailJsonRes("one of the pre processors commands returning a fail."));
                return;
            } else {
                Map sets = (Map) preRetObject.get(OUT_SET);
                if (null != sets) {
                    for (Object key : sets.keySet()) {
                        httpContext.setAttribute((String) key, sets.get(key));
                    }
                }

                List removes = (List) preRetObject.get(OUT_REMOVE);
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
                params(parameterMap, config, reqMap);
            }

            if (config.isNeedBody()) {
                reqMap.put("body", HttpUtils.getBodyAsString(httpRequest));
            }

            if (config.isNeedRetObj()) {
                Object retObject = httpContext.getAttribute(RET_OBJECT);
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
            if (Integer.parseInt(postRetObject.get(CODE).toString()) == CODE_INVALID) {

                HttpUtils.response(httpResponse, JsonRes.getFailJsonRes("one of the post processors commands returning a fail."));
                return;
            } else if (Integer.parseInt(postRetObject.get(CODE).toString()) == CODE_REPLACE_VALID) {

                httpContext.setAttribute(RET_OBJECT_STRING, postRetObject.get(BODY));
            }
        }

        Object retObjectString = httpContext.getAttribute(RET_OBJECT_STRING);
        if (null != retObjectString) {

            HttpUtils.response(httpResponse, (String) retObjectString);
        } else {

            Object retObject = httpContext.getAttribute(RET_OBJECT);
            if (null != retObject) {
                HttpUtils.response(httpResponse, new JsonRes<>(CODE_VALID, retObject));
            }
        }

    }

    private void params(Map<String, String> parameterMap, PostConfig config, Map reqMap) {
        this.params(parameterMap, config.getParameters(), reqMap);
    }

    private void params(Map<String, String> parameterMap, PreConfig config, Map reqMap) {
        this.params(parameterMap, config.getParameters(), reqMap);
    }

    private void params(Map<String, String> parameterMap, List<String> keys, Map reqMap) {
        Map parameters = new HashMap();
        for (String param : keys) {
            parameters.put(param, parameterMap.get(param));
        }
        reqMap.put("parameters", parameters);
    }

}
