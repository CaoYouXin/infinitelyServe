package tech.caols.infinitely.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.pool.BasicPoolEntry;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.config.PrePostConfig;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.server.SimplePool;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyHandler implements HttpRequestHandler {

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
        System.out.println(reqHeaderName + " : " + Arrays.toString(headers));

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

        boolean abort = false;
        BasicPoolEntry connEntry = null;
        ObjectMapper objectMapper = new ObjectMapper();

        String url = HttpUtils.getSimpleDecodedUrl(httpRequest);
        PrePostConfig.Config config = PrePostConfig.get().getConfig(url, Constants.PRE_PROCESSOR);

        if (null != config) {
            connEntry = SimplePool.get().getConn(config.getHost());
            HttpClientConnection conn = connEntry.getConnection();
            HttpCoreContext coreContext = HttpCoreContext.create();
            coreContext.setTargetHost(config.getHost());

            Map map = new HashMap();

            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", "/pre_request");
            request.setEntity(new StringEntity(objectMapper.writeValueAsString(map), ContentType.APPLICATION_JSON));
            System.out.println(">> Request URI: " + request.getRequestLine().getUri());

            this.httpExecutor.preProcess(request, this.httpproc, coreContext);
            HttpResponse response = this.httpExecutor.execute(request, conn, coreContext);
            this.httpExecutor.postProcess(response, this.httpproc, coreContext);

            System.out.println("<< Response: " + response.getStatusLine());
            String preRet = EntityUtils.toString(response.getEntity());
            System.out.println(preRet);
            System.out.println("==============");

            HashMap preRetObject = objectMapper.readValue(preRet, HashMap.class);
            if (preRetObject.get(Constants.CODE).equals(Constants.INVALID)) {
                abort = true;
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

        if (abort) {
            httpResponse.setStatusCode(HttpStatus.SC_OK);
            httpResponse.setEntity(new StringEntity(
                    "{\"code\":" + Constants.CODE_INVALID + "}",
                    ContentType.APPLICATION_JSON
            ));
            return;
        }

        this.handler.handle(httpRequest, httpResponse, httpContext);

//        config = PrePostConfig.get().getConfig(url, Register.POST_PROCESSOR);
//        if (null != config) {
//            connEntry = SimplePool.get().getConn(config.getHost());
//            HttpClientConnection conn = connEntry.getConnection();
//
//            this.coreContext.setTargetHost(config.getHost());
//
//            Map map = new HashMap();
//
//            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", "/pre_request");
//            request.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(map), ContentType.APPLICATION_JSON));
//            System.out.println(">> Request URI: " + request.getRequestLine().getUri());
//
//            httpExecutor.preProcess(request, httpproc, coreContext);
//            HttpResponse response = httpExecutor.execute(request, conn, coreContext);
//            httpExecutor.postProcess(response, httpproc, coreContext);
//
//            System.out.println("<< Response: " + response.getStatusLine());
//            System.out.println(EntityUtils.toString(response.getEntity()));
//            System.out.println("==============");
//        }

        if (null != connEntry) {
            SimplePool.get().release(connEntry);
        }

        Object retObject = httpContext.getAttribute(Constants.RET_OBJECT);
        if (null != retObject) {
            HttpUtils.response(httpResponse, new JsonRes<>(Constants.CODE_VALID, retObject));
        }
    }

}
