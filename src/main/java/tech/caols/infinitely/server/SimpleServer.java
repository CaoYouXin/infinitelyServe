package tech.caols.infinitely.server;

import org.apache.commons.io.IOUtils;
import org.apache.http.ConnectionClosedException;
import org.apache.http.Consts;
import org.apache.http.ExceptionLogger;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

public class SimpleServer {

    private final ServerBootstrap serverBootstrap;
    private HttpServer server;

    public SimpleServer(int port) {
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(15000)
                .setTcpNoDelay(true)
                .build();

        this.serverBootstrap = ServerBootstrap.bootstrap()
                .setListenerPort(port)
                .setServerInfo("Test/1.1")
                .setSocketConfig(socketConfig)
                .setExceptionLogger(new StdErrorExceptionLogger());
    }

    public SimpleServer registerHandler(String pattern, HttpRequestHandler handler) {
        this.serverBootstrap.registerHandler(pattern, handler);
        return this;
    }

    public void start(CallBack callBack) {
        final HttpServer _server = this.server = this.serverBootstrap.create();

        try {
            _server.start();

            String stopToken = Math.random() + "";
            stopToken = "123456789012345678901234567890".substring(stopToken.length()) + stopToken;
            IOUtils.write(stopToken, new FileOutputStream(new File("start.log")), Consts.UTF_8);

            String toString = IOUtils.toString(new FileInputStream(new File("start.log")));
            System.out.println("to string : " + toString);

            callBack.call();

            _server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> _server.shutdown(5, TimeUnit.SECONDS)));
    }

    public void shutdown() {
        this.server.shutdown(5, TimeUnit.SECONDS);
    }

    public interface CallBack {
        void call();
    }

    private static class StdErrorExceptionLogger implements ExceptionLogger {

        @Override
        public void log(final Exception ex) {
            if (ex instanceof SocketTimeoutException) {
                System.err.println("Connection timed out");
            } else if (ex instanceof ConnectionClosedException) {
                System.err.println(ex.getMessage());
            } else {
                ex.printStackTrace();
            }
        }

    }

}
