package tech.caols.infinitely.server.handlers;

import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
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

        if (!file.exists()) {

            httpResponse.setStatusCode(HttpStatus.SC_NOT_FOUND);
            StringEntity entity = new StringEntity(
                    "<html><body><h1>File" + file.getPath() +
                            " not found</h1></body></html>",
                    ContentType.create("text/html", "UTF-8"));
            httpResponse.setEntity(entity);
            System.out.println("File " + file.getPath() + " not found");

        } else if (file.isDirectory()) {

            final File indexFile = new File(file.getAbsolutePath(), "index.html");
            this.processFile(httpResponse, httpContext, indexFile);

        } else if (!file.canRead()) {

            httpResponse.setStatusCode(HttpStatus.SC_FORBIDDEN);
            StringEntity entity = new StringEntity(
                    "<html><body><h1>Access denied</h1></body></html>",
                    ContentType.create("text/html", "UTF-8"));
            httpResponse.setEntity(entity);
            System.out.println("Cannot read file " + file.getPath());

        } else {
            HttpCoreContext coreContext = HttpCoreContext.adapt(httpContext);
            HttpConnection conn = coreContext.getConnection(HttpConnection.class);
            httpResponse.setStatusCode(HttpStatus.SC_OK);
            FileEntity body = new FileEntity(file, ContentType.create("text/html", (Charset) null));
            httpResponse.setEntity(body);
            System.out.println(conn + ": serving file " + file.getPath());
        }

    }

}
