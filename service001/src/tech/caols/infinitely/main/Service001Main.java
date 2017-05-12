package tech.caols.infinitely.main;

import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.ShutDownConfig;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.handlers.UserAddHandler;
import tech.caols.infinitely.server.SimpleServer;
import tech.caols.infinitely.server.Stopper;

public class Service001Main {

    public static void main(String[] args) {
        ConfigUtil util = new ConfigUtil();

        switch (args[0]) {
            case "start":

                final SimpleConfig config = util.getConfigFromFile(util.getRootFileName() + ".json", SimpleConfig.class);
                SimpleServer simpleServer = new SimpleServer(config.getPort(), config.getDocRoot())
                        .registerHandler("/user/add", new UserAddHandler());
                simpleServer.start(() -> {
                    System.out.println("service 001 started.");
                    System.out.println(config);
                });

                break;
            case "stop":

                ShutDownConfig shutDownConfig = util.getConfigFromFile(util.getRootFileName() + ".log", ShutDownConfig.class);
                new Stopper(shutDownConfig.getHostName(), shutDownConfig.getHostPort(), shutDownConfig.getToken()).call();

                break;
            default:
                break;
        }
    }

}
