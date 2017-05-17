package tech.caols.infinitely.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.cmd.Stop;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.handlers.ConfigServerHandler;
import tech.caols.infinitely.handlers.GetServerConfigHandler;
import tech.caols.infinitely.handlers.ListServerHandler;
import tech.caols.infinitely.handlers.ServerManipulationHandler;
import tech.caols.infinitely.server.handlers.UploadHandler;
import tech.caols.infinitely.server.SimpleServer;
import tech.caols.infinitely.server.handlers.ProxyHandler;


public class ServerManagementMain {

    static {
        System.setProperty("log4j.configurationFile", "log4j2.xml");
    }

    private static final Logger logger = LogManager.getLogger(ServerManagementMain.class);

    public static void main(String[] args) {
        final ConfigUtil util = new ConfigUtil();

        SimpleUtils.main(args, () -> {
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
                logger.info("service server management started.");
                logger.info(config);
            });
        }, new Stop(util));
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
