package tech.caols.infinitely.server;

import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.register.Register;

public class SimpleServerTest {

    public static void main(String[] args) {
        ConfigUtil util = new ConfigUtil();
        final SimpleConfig config = util.getConfig("conf.json", SimpleConfig.class);
        SimpleServer simpleServer = new SimpleServer(config.getPort(), config.getDocRoot());
        simpleServer.start(() -> {
            System.out.println("simple server test on.");
            System.out.println(config);
        });
    }

}
