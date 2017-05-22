package tech.caols.infinitely.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.cmd.Stop;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.controllers.ResourceController;
import tech.caols.infinitely.rest.RestHelper;
import tech.caols.infinitely.server.SimpleServer;

public class ServiceResourcesMain {

    private static final Logger logger = LogManager.getLogger(ServiceResourcesMain.class);

    public static void main(String[] args) {
        final ConfigUtil util = new ConfigUtil();

        SimpleUtils.main(args, () -> {
            final Config config = util.getConfigFromFile(util.getRootFileName() + ".json", Config.class);
            SimpleServer simpleServer = new SimpleServer(config.getServer().getPort(), config.getServer().getDocRoot());
            RestHelper restHelper = new RestHelper(simpleServer);
            restHelper.addRestObject(new ResourceController(config.getSourceRoot()));

            simpleServer.start(() -> {
                logger.info("service [ServiceResourcesMain] started.");
                logger.info(config);
            });
        }, new Stop(util));
    }

    static class Config {
        private SimpleConfig server;
        private String sourceRoot;

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
    }

}
