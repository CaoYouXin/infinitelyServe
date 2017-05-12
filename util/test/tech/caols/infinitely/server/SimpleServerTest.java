package tech.caols.infinitely.server;

import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.SimpleConfig;

public class SimpleServerTest {

    public static void main(String[] args) {
        ConfigUtil util = new ConfigUtil();
//        final SimpleConfig config = util.getConfigFromFile("conf.json", SimpleConfig.class);
//        SimpleServer simpleServer = new SimpleServer(config.getPort(), config.getDocRoot());
//        simpleServer.start(() -> {
//            System.out.println("simple server test on.");
//            System.out.println(config);
//        });

//        util.loadConfig("test.json");
        Config config = util.getConfigFromFile("test.json", Config.class);
        System.out.println(config);
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
