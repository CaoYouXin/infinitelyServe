package tech.caols.infinitely.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.CallBack;
import tech.caols.infinitely.config.PreConfig;
import tech.caols.infinitely.config.PreConfigs;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

public class PreRegister implements CallBack {

    private static final Logger logger = LogManager.getLogger(PreRegister.class);

    private final PreConfigs preConfigs;
    private final String hostName;
    private final int port;

    public PreRegister(PreConfigs preConfigs, String hostName, int port) {
        this.preConfigs = preConfigs;
        this.hostName = hostName;
        this.port = port;
//        try {
//            this.hostName = SimpleUtils.getLocalHostLANAddress().getHostAddress();
//        } catch (UnknownHostException e) {
//            logger.catching(e);
//        }
    }

    @Override
    public void call() {
        HttpHost host = new HttpHost(this.preConfigs.getHostName(), this.preConfigs.getPort());

        HttpProcessor httpProcessor = HttpProcessorBuilder.create()
                .add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestUserAgent("Test/1.1"))
                .add(new RequestExpectContinue(true)).build();

        HttpRequestExecutor httpExecutor = new HttpRequestExecutor();

        HttpCoreContext coreContext = HttpCoreContext.create();
        coreContext.setTargetHost(host);

        DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);

        String uri = "/hook.cfg?type=PRE";
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            for (PreConfigs.PreConfigItem next : this.preConfigs.getItems()) {
                PreConfig preConfig = new PreConfig();
                preConfig.setHostName(this.hostName);
                preConfig.setPort(this.port);
                preConfig.setRegexStr(next.getRegex());
                preConfig.setNeedBody(next.isNeedBody());
                preConfig.setParameters(next.getParameters());
                preConfig.setUrl(next.getUrl());

                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket);
                }

                BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", uri);
                request.setEntity(new StringEntity(objectMapper.writeValueAsString(preConfig), ContentType.APPLICATION_JSON));

                logger.info(">> Request URI: " + request.getRequestLine().getUri());

                httpExecutor.preProcess(request, httpProcessor, coreContext);
                HttpResponse response = httpExecutor.execute(request, conn, coreContext);
                httpExecutor.postProcess(response, httpProcessor, coreContext);

                logger.info("<< Response: " + response.getStatusLine());
                logger.info(EntityUtils.toString(response.getEntity()));
                logger.info("==============");
            }

        } catch (IOException | HttpException e) {
            logger.catching(e);
        } finally {
            try {
                conn.close();
            } catch (IOException e) {
                logger.catching(e);
            }
        }
    }
}
