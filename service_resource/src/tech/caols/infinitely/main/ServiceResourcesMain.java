package tech.caols.infinitely.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.cmd.Stop;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.controllers.LevelController;
import tech.caols.infinitely.controllers.LeveledResourceController;
import tech.caols.infinitely.controllers.ResourceController;
import tech.caols.infinitely.handlers.ResourceHandler;
import tech.caols.infinitely.rest.RestHelper;
import tech.caols.infinitely.server.SimpleServer;
import tech.caols.infinitely.server.handlers.HttpFileHandler;
import tech.caols.infinitely.server.handlers.ProxyHandler;

public class ServiceResourcesMain {

    private static final Logger logger = LogManager.getLogger(ServiceResourcesMain.class);

    public static void main(String[] args) {
        final ConfigUtil util = new ConfigUtil();

        SimpleUtils.main(args, () -> {
            final Config config = util.getConfigFromFile(util.getRootFileName() + ".json", Config.class);
            SimpleServer simpleServer = new SimpleServer(config.getServer().getPort(), config.getServer().getDocRoot())
                    .registerHandler("/r/*", new ProxyHandler(
                            new ResourceHandler(config.getResourceRoot(), "/r")
                    ));

            RestHelper restHelper = new RestHelper(simpleServer);
            restHelper.addRestObject(new ResourceController(config.getSourceRoot(), config.getResourceRoot()))
                    .addRestObject(new LevelController()).addRestObject(new LeveledResourceController());

            simpleServer.start(() -> {
                logger.info("service [ServiceResourcesMain] started.");
                logger.info(config);
            });
        }, new Stop(util));
    }

    static class Config {
        private SimpleConfig server;
        private String sourceRoot;
        private String resourceRoot;

        public SimpleConfig getServer() {
            return server;
        }

        public void setServer(SimpleConfig server) {
            this.server = server;
        }

        public String getSourceRoot() {
            return sourceRoot;
        }

        public void setSourceRoot(String sourceRoot) {
            this.sourceRoot = sourceRoot;
        }

        public String getResourceRoot() {
            return resourceRoot;
        }

        public void setResourceRoot(String resourceRoot) {
            this.resourceRoot = resourceRoot;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "server=" + server +
                    ", sourceRoot='" + sourceRoot + '\'' +
                    ", resourceRoot='" + resourceRoot + '\'' +
                    '}';
        }
    }

}
