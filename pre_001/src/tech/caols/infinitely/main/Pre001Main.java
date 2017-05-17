package tech.caols.infinitely.main;

import org.apache.http.HttpHost;
import tech.caols.infinitely.CallBacks;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.cmd.Stop;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.ShutDownConfig;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.hanlders.PreHandler;
import tech.caols.infinitely.register.Register;
import tech.caols.infinitely.server.SimpleServer;
import tech.caols.infinitely.server.Stopper;

import java.util.Arrays;
import java.util.List;

public class Pre001Main {

    public static void main(String[] args) {
        final ConfigUtil util = new ConfigUtil();

        SimpleUtils.main(args, () -> {
            Config config = util.getConfigFromFile(util.getRootFileName() + ".json", Config.class);
            System.out.println(config);

            SimpleServer simpleServer = new SimpleServer(config.getServer().getPort(), config.getServer().getDocRoot());
            simpleServer.registerHandler("/pre_request", new PreHandler());

            CallBacks callBacks = new CallBacks();
            for (RegisterConfig registerConfig: config.getRegisters()) {
                for (UrlConfig url: registerConfig.getUrls()) {
                    Register register = new Register(new HttpHost(registerConfig.getHost().getName(),
                            registerConfig.getHost().getPort()),
                            Constants.PRE_PROCESSOR, url.getUrl(), config.getServer().getPort());

                    if (null != url.getParameters() && !"null".equals(url.getParameters())) {
                        register.setParamters(url.getParameters());
                    }

                    if (null != url.getNeedBody()) {
                        register.setNeedBody(url.getNeedBody());
                    }

                    callBacks.add(register);
                }
            }
            simpleServer.start(callBacks);
        }, new Stop(util));
    }

    static class Config {
        private SimpleConfig server;
        private List<RegisterConfig> registers;

        public SimpleConfig getServer() {
            return server;
        }

        public void setServer(SimpleConfig server) {
            this.server = server;
        }

        public List<RegisterConfig> getRegisters() {
            return registers;
        }

        public void setRegisters(List<RegisterConfig> registers) {
            this.registers = registers;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "server=" + server +
                    ", registers=" + registers +
                    '}';
        }
    }

    static class RegisterConfig {
        private List<UrlConfig> urls;
        private SimpleHost host;

        public List<UrlConfig> getUrls() {
            return urls;
        }

        public void setUrls(List<UrlConfig> urls) {
            this.urls = urls;
        }

        public SimpleHost getHost() {
            return host;
        }

        public void setHost(SimpleHost host) {
            this.host = host;
        }

        @Override
        public String toString() {
            return "RegisterConfig{" +
                    "urls=" + Arrays.toString(urls.toArray()) +
                    ", host=" + host +
                    '}';
        }
    }

    static class UrlConfig {
        private String url;
        private String parameters;
        private Boolean needBody;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getParameters() {
            return parameters;
        }

        public void setParameters(String parameters) {
            this.parameters = parameters;
        }

        public Boolean getNeedBody() {
            return needBody;
        }

        public void setNeedBody(Boolean needBody) {
            this.needBody = needBody;
        }

        @Override
        public String toString() {
            return "UrlConfig{" +
                    "url='" + url + '\'' +
                    ", parameters='" + parameters + '\'' +
                    ", needBody=" + needBody +
                    '}';
        }
    }

    static class SimpleHost {
        private String name;
        private int port;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "SimpleHost{" +
                    "name='" + name + '\'' +
                    ", port=" + port +
                    '}';
        }
    }

}
