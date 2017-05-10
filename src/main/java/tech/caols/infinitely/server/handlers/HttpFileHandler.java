package tech.caols.infinitely.server.handlers;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;

public class HttpFileHandler implements HttpRequestHandler {

    private final String docRoot;

    public HttpFileHandler(String docRoot) {
        super();
        this.docRoot = docRoot;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        String method = httpRequest.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
        if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
            throw new MethodNotSupportedException(method + " method not supported");
        }
        String target = httpRequest.getRequestLine().getUri();

        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
            byte[] entityContent = EntityUtils.toByteArray(entity);
            System.out.println("Incoming entity content (bytes): " + entityContent.length);
        }

        String decode = URLDecoder.decode(target, "UTF-8");
        final File file = new File(this.docRoot, decode);
        this.processFile(httpResponse, httpContext, file);
    }

    private void processFile(HttpResponse httpResponse, HttpContext httpContext, File file) {

        System.out.println(file.getAbsolutePath());

    }

}
