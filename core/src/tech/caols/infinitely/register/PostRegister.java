package tech.caols.infinitely.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.CallBack;
import tech.caols.infinitely.config.PostConfig;
import tech.caols.infinitely.config.PostConfigs;
import tech.caols.infinitely.config.PreConfig;
import tech.caols.infinitely.config.PreConfigs;

import java.io.IOException;
import java.net.Socket;

public class PostRegister implements CallBack {

    private static final Logger logger = LogManager.getLogger(PostRegister.class);

    private final PostConfigs postConfigs;
    private final String hostName;
    private final int port;

    public PostRegister(PostConfigs postConfigs, String hostName, int port) {
        this.postConfigs = postConfigs;
        this.hostName = hostName;
        this.port = port;
    }

    @Override
    public void call() {
        HttpHost host = new HttpHost(this.postConfigs.getHostName(), this.postConfigs.getPort());

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

        String uri = "/hook.cfg?type=POST";
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            for (PostConfigs.PostConfigItem next : this.postConfigs.getItems()) {
                PostConfig postConfig = new PostConfig();
                postConfig.setHostName(this.hostName);
                postConfig.setPort(this.port);
                postConfig.setRegexStr(next.getRegex());
                postConfig.setNeedBody(next.isNeedBody());
                postConfig.setNeedRetObj(next.isNeedRetObj());
                postConfig.setParameters(next.getParameters());
                postConfig.setUrl(next.getUrl());

                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket);
                }

                BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", uri);
                request.setEntity(new StringEntity(objectMapper.writeValueAsString(postConfig), ContentType.APPLICATION_JSON));

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
