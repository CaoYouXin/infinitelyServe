package tech.caols.infinitely.rest;

import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import tech.caols.infinitely.server.HttpUtils;

@Rest
public class RestObject {

    @RestAPI(name="test", url="/user/u", target=RestTarget.GET)
    public Object handler1(HttpRequest request, HttpContext context) {
        System.out.println(HttpUtils.getMethod(request));
        return null;
    }

}
