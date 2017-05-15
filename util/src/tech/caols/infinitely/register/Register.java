package tech.caols.infinitely.register;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;
import tech.caols.infinitely.CallBack;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.SimpleUtils;

import java.io.IOException;
import java.net.Socket;

public class Register implements CallBack {

    private HttpHost host;
    private String type;
    private String url;
    private int port;
    private String parameters;
    private Boolean needBody;

    public Register(HttpHost host, String type, String url, int port) throws WrongRegisterTypeException {

        if (!type.equals(Constants.PRE_PROCESSOR) && !type.equals(Constants.POST_PROCESSOR)) {
            throw new WrongRegisterTypeException("wrong type : " + type);
        }

        this.type = type;
        this.url = url;
        this.host = host;
        this.port = port;
    }

    public Register setParamters(String parameters) {
        this.parameters = parameters;
        return this;
    }

    public Register setNeedBody(Boolean needBody) {
        this.needBody = needBody;
        return this;
    }

    @Override
    public void call() {
        HttpProcessor httpproc = HttpProcessorBuilder.create()
                .add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestUserAgent("Test/1.1"))
                .add(new RequestExpectContinue(true)).build();

        HttpRequestExecutor httpExecutor = new HttpRequestExecutor();

        HttpCoreContext coreContext = HttpCoreContext.create();
        coreContext.setTargetHost(this.host);

        DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);

        try {

            if (!conn.isOpen()) {
                Socket socket = new Socket(this.host.getHostName(), this.host.getPort());
                conn.bind(socket);
            }
            String uri = "/" + this.type + ".cfg?loc="
                    + SimpleUtils.getLocalHostLANAddress().getHostAddress()
                    + "&port=" + this.port + "&url=" + this.url;

            if (null != this.parameters) {
                uri += "&parameters=" + this.parameters;
            }

            if (null != this.needBody) {
                uri += "&needBody=" + Boolean.toString(this.needBody);
            }

            BasicHttpRequest request = new BasicHttpRequest("GET", uri);
            System.out.println(">> Request URI: " + request.getRequestLine().getUri());

            httpExecutor.preProcess(request, httpproc, coreContext);
            HttpResponse response = httpExecutor.execute(request, conn, coreContext);
            httpExecutor.postProcess(response, httpproc, coreContext);

            System.out.println("<< Response: " + response.getStatusLine());
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println("==============");
        } catch (IOException | HttpException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
