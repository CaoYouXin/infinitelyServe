package tech.caols.infinitely.handlers;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;

import java.io.*;

public class ConfigServerHandler implements HttpRequestHandler {

    private final String serverRoot;

    public ConfigServerHandler(String serverRoot) {
        this.serverRoot = serverRoot;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!HttpUtils.isPost(httpRequest)) {
            throw new MethodNotSupportedException("not a POST method, but a " + HttpUtils.getMethod(httpRequest));
        }

        Config bodyAsObject = HttpUtils.getBodyAsObject(httpRequest, Config.class);
        if (null == bodyAsObject) {
            HttpUtils.response(httpResponse, JsonRes.getFailJsonRes("request body incorrect"));
            return;
        }

        String serverFileName = bodyAsObject.getServerName().substring(0, bodyAsObject.getServerName().indexOf("_jar"));
        File file = new File(this.serverRoot + bodyAsObject.getServerName(), serverFileName + ".json");
        if (!file.exists()) {
            if (!file.createNewFile()) {
                HttpUtils.response(httpResponse, JsonRes.getFailJsonRes("can not create config file"));
                return;
            }
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        bufferedWriter.write(bodyAsObject.getConfigs());
        bufferedWriter.flush();
        bufferedWriter.close();
        HttpUtils.response(httpResponse, JsonRes.SuccessJsonRes);
    }

    static class Config {
        private String serverName;
        private String configs;

        @Override
        public String toString() {
            return "Config{" +
                    "serverName='" + serverName + '\'' +
                    ", configs='" + configs + '\'' +
                    '}';
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getConfigs() {
            return configs;
        }

        public void setConfigs(String configs) {
            this.configs = configs;
        }
    }

}
