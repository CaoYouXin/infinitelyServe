package tech.caols.infinitely.hanlders;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import tech.caols.infinitely.Constants;

import java.io.IOException;

public class PreHandler implements HttpRequestHandler {
    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        System.out.println("pre 001 handle");

        httpResponse.setStatusCode(HttpStatus.SC_OK);
        httpResponse.setEntity(new StringEntity("{\"code\":\""+ Constants.INVALID +"\"}", ContentType.APPLICATION_JSON));
    }
}
