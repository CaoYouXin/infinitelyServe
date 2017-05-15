package tech.caols.infinitely.server;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.*;

import java.io.IOException;
import java.net.Socket;

public class Stopper {

    private final HttpHost host;
    private final BasicHttpRequest request;
    private final HttpProcessor httpproc;
    private final HttpRequestExecutor httpExecutor;

    public Stopper(String host, int port, String token) {
        this.host = new HttpHost(host, port);
        this.request = new BasicHttpRequest("GET", "/shutdown.cmd?token=" + token);

        this.httpproc = HttpProcessorBuilder.create()
                .add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestUserAgent("Test/1.1"))
                .add(new RequestExpectContinue(true)).build();

        this.httpExecutor = new HttpRequestExecutor();
    }

    public void call() {
        HttpCoreContext coreContext = HttpCoreContext.create();
        coreContext.setTargetHost(this.host);
        DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
        try {
            if (!conn.isOpen()) {
                Socket socket = new Socket(this.host.getHostName(), this.host.getPort());
                conn.bind(socket);
            }

            System.out.println("request => " + this.request.getRequestLine());

            this.httpExecutor.preProcess(this.request, this.httpproc, coreContext);
            HttpResponse httpResponse = this.httpExecutor.execute(this.request, conn, coreContext);
            this.httpExecutor.postProcess(httpResponse, this.httpproc, coreContext);

            System.out.println("response => " + httpResponse.getStatusLine());

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
