package tech.caols.infinitely.server;

import org.apache.http.ConnectionClosedException;
import org.apache.http.ExceptionLogger;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.CallBack;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.ShutDownConfig;
import tech.caols.infinitely.server.handlers.CfgHandler;
import tech.caols.infinitely.server.handlers.HttpFileHandler;
import tech.caols.infinitely.server.handlers.ProxyHandler;
import tech.caols.infinitely.server.handlers.ShutDownHandler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

public class SimpleServer {

    private static final Logger logger = LogManager.getLogger(SimpleServer.class);

    private final ServerBootstrap serverBootstrap;
    private final int port;
    private HttpServer server;

    public SimpleServer(int port, String docRoot) {
        this.port = port;

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(15000)
                .setTcpNoDelay(true)
                .build();

        this.serverBootstrap = ServerBootstrap.bootstrap()
                .setListenerPort(port)
                .setServerInfo("Test/1.1")
                .setSocketConfig(socketConfig)
                .setExceptionLogger(new StdErrorExceptionLogger())
                .registerHandler("/hook.cfg", new CfgHandler())
                .registerHandler("/shutdown.cmd", new ShutDownHandler(this))
                .registerHandler("*", new ProxyHandler(new HttpFileHandler(docRoot, "")));
    }

    public SimpleServer registerHandler(String pattern, HttpRequestHandler handler) {
        this.serverBootstrap.registerHandler(pattern, handler);
        return this;
    }

    public void start(CallBack callBack) {
        final HttpServer _server = this.server = this.serverBootstrap.create();

        try {
            _server.start();

            String stopToken = 1000 * Math.random() + "";
            stopToken = "123456789012345678901234567890".substring(stopToken.length()) + stopToken;

            ShutDownConfig config = new ShutDownConfig();
            config.setToken(stopToken);
            config.setHostName(SimpleUtils.getLocalHostLANAddress().getHostAddress());
            config.setHostPort(this.port);

            ConfigUtil util = new ConfigUtil();
            util.setConfig(util.getRootFileName() + ".log", config);

            ShutDownConfig shutDownConfig = util.getConfigFromFile(util.getRootFileName() + ".log", ShutDownConfig.class);
            logger.info(shutDownConfig);

            callBack.call();

            _server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException | IOException e) {
            logger.catching(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            _server.shutdown(5, TimeUnit.SECONDS);
            logger.info("server will shutdown in 5 seconds...");
        }));
    }

    public void shutdown() {
        this.server.shutdown(5, TimeUnit.SECONDS);
        SimplePool.get().shutdown();
    }

    private static class StdErrorExceptionLogger implements ExceptionLogger {

        @Override
        public void log(final Exception ex) {
            if (ex instanceof SocketTimeoutException) {
                logger.error("Connection timed out");
            } else if (ex instanceof ConnectionClosedException) {
                logger.error(ex.getMessage());
            } else {
                logger.catching(ex);
            }
        }

    }

}
