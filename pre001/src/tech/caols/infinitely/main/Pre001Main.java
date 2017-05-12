package tech.caols.infinitely.main;

import org.apache.http.HttpHost;
import tech.caols.infinitely.CallBacks;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.ShutDownConfig;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.register.Register;
import tech.caols.infinitely.server.SimpleServer;
import tech.caols.infinitely.server.Stopper;

import java.util.List;

public class Pre001Main {

    public static void main(String[] args) {
        ConfigUtil util = new ConfigUtil();

        switch (args[0]) {
            case "start":

                Config config = util.getConfigFromFile(util.getRootFileName() + ".json", Config.class);
                System.out.println(config);

                SimpleServer simpleServer = new SimpleServer(config.getServer().getPort(), config.getServer().getDocRoot());

                CallBacks callBacks = new CallBacks();
                for (RegisterConfig registerConfig: config.getRegisters()) {
                    for (String url: registerConfig.getUrls()) {
                        callBacks.add(new Register(new HttpHost(registerConfig.getHost().getName(),
                                registerConfig.getHost().getPort()),
                                Register.PRE_PROCESSOR, url, config.getServer().getPort()));
                    }
                }
                simpleServer.start(callBacks);

                break;
            case "stop":

                ShutDownConfig shutDownConfig = util.getConfigFromFile(util.getRootFileName() + ".log", ShutDownConfig.class);
                new Stopper(shutDownConfig.getHostName(), shutDownConfig.getHostPort(), shutDownConfig.getToken()).call();

                break;
            default:
                break;
        }
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
        private List<String> urls;
        private SimpleHost host;

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }

        public SimpleHost getHost() {
            return host;
        }

        public void setHost(SimpleHost host) {
            this.host = host;
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
