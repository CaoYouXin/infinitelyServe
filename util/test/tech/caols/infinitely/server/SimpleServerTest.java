package tech.caols.infinitely.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.SimpleConfig;

public class SimpleServerTest {

    private static final Logger logger = LogManager.getLogger(SimpleServerTest.class);

    public static void main(String[] args) {
        ConfigUtil util = new ConfigUtil();
//        final SimpleConfig config = util.getConfigFromFile("conf.json", SimpleConfig.class);
//        SimpleServer simpleServer = new SimpleServer(config.getPort(), config.getDocRoot());
//        simpleServer.start(() -> {
//            logger.info("simple server test on.");
//            logger.info(config);
//        });

//        util.loadConfig("test.json");
        Config config = util.getConfigFromFile("test.json", Config.class);
        logger.info(config);
    }

    static class Config {
        private SimpleConfig server;

        public SimpleConfig getServer() {
            return server;
        }

        public void setServer(SimpleConfig server) {
            this.server = server;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "server=" + server +
                    '}';
        }
    }

}
