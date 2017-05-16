package tech.caols.infinitely.main;

import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.ShutDownConfig;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.handlers.ConfigServerHandler;
import tech.caols.infinitely.handlers.GetServerConfigHandler;
import tech.caols.infinitely.handlers.ListServerHandler;
import tech.caols.infinitely.handlers.ServerManipulationHandler;
import tech.caols.infinitely.server.handlers.UploadHandler;
import tech.caols.infinitely.server.SimpleServer;
import tech.caols.infinitely.server.Stopper;
import tech.caols.infinitely.server.handlers.ProxyHandler;

public class ServerManagementMain {

    public static void main(String[] args) {
        ConfigUtil util = new ConfigUtil();

        switch (args[0]) {
            case "start":

                final Config config = util.getConfigFromFile(util.getRootFileName() + ".json", Config.class);
                SimpleServer simpleServer = new SimpleServer(config.getServer().getPort(), config.getServer().getDocRoot())
                        .registerHandler("/server/list", new ProxyHandler(
                                new ListServerHandler(config.getServerRoot(), config.getUploadRoot())
                        )).registerHandler("/server/config/get", new ProxyHandler(
                                new GetServerConfigHandler(config.getServerRoot())
                        )).registerHandler("/server/config", new ProxyHandler(
                                new ConfigServerHandler(config.getServerRoot())
                        )).registerHandler("/server/manipulation", new ProxyHandler(
                                new ServerManipulationHandler(config.getServerRoot(), config.getUploadRoot())
                        ))
                        .registerHandler("/upload", new ProxyHandler(
                                new UploadHandler(config.getUploadRoot())
                        ));
                simpleServer.start(() -> {
                    System.out.println("service server management started.");
                    System.out.println(config);
                });

                break;
            case "stop":

                String fileName = util.getRootFileName() + ".log";

                ShutDownConfig shutDownConfig = util.getConfigFromFile(fileName, ShutDownConfig.class);
                new Stopper(shutDownConfig.getHostName(), shutDownConfig.getHostPort(), shutDownConfig.getToken()).call();

                util.eraseConfigFile(fileName);
                break;
            default:
                break;
        }
    }

    static class Config {
        private SimpleConfig server;
        private String uploadRoot;
        private String serverRoot;

        public String getServerRoot() {
            return serverRoot;
        }

        public void setServerRoot(String serverRoot) {
            this.serverRoot = serverRoot;
        }

        public SimpleConfig getServer() {
            return server;
        }

        public void setServer(SimpleConfig server) {
            this.server = server;
        }

        public String getUploadRoot() {
            return uploadRoot;
        }

        public void setUploadRoot(String uploadRoot) {
            this.uploadRoot = uploadRoot;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "server=" + server +
                    ", uploadRoot='" + uploadRoot + '\'' +
                    ", serverRoot='" + serverRoot + '\'' +
                    '}';
        }
    }

}
