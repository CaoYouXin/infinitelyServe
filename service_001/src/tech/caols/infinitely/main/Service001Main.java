package tech.caols.infinitely.main;

import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.ShutDownConfig;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.handlers.UserAddHandler;
import tech.caols.infinitely.server.SimpleServer;
import tech.caols.infinitely.server.Stopper;
import tech.caols.infinitely.server.handlers.ProxyHandler;

public class Service001Main {

    public static void main(String[] args) {
        ConfigUtil util = new ConfigUtil();

        switch (args[0]) {
            case "start":

                final SimpleConfig config = util.getConfigFromFile(util.getRootFileName() + ".json", SimpleConfig.class);
                SimpleServer simpleServer = new SimpleServer(config.getPort(), config.getDocRoot())
                        .registerHandler("/user/add", new ProxyHandler(new UserAddHandler()));
                simpleServer.start(() -> {
                    System.out.println("service 001 started.");
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

}
