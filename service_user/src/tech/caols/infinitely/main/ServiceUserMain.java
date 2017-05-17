package tech.caols.infinitely.main;

import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.cmd.Stop;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.ShutDownConfig;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.handlers.ListUserHandler;
import tech.caols.infinitely.server.SimpleServer;
import tech.caols.infinitely.server.Stopper;
import tech.caols.infinitely.server.handlers.ProxyHandler;

public class ServiceUserMain {

    public static void main(String[] args) {
        final ConfigUtil util = new ConfigUtil();

        SimpleUtils.main(args, () -> {
            final SimpleConfig config = util.getConfigFromFile(util.getRootFileName() + ".json", SimpleConfig.class);
            SimpleServer simpleServer = new SimpleServer(config.getPort(), config.getDocRoot())
                    .registerHandler("/user/list", new ProxyHandler(
                            new ListUserHandler()
                    ));
            simpleServer.start(() -> {
                System.out.println("service [ServiceUserMain] started.");
                System.out.println(config);
            });
        }, new Stop(util));
    }

}