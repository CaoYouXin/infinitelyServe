package tech.caols.infinitely.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.cmd.Stop;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.controllers.CaptchaController;
import tech.caols.infinitely.controllers.UserController;
import tech.caols.infinitely.rest.RestHelper;
import tech.caols.infinitely.server.SimpleServer;

public class ServiceUserMain {

    private static final Logger logger = LogManager.getLogger(ServiceUserMain.class);

    public static void main(String[] args) {
        final ConfigUtil util = new ConfigUtil();

        SimpleUtils.main(args, () -> {
            final SimpleConfig config = util.getConfigFromFile(util.getRootFileName() + ".json", SimpleConfig.class);
            SimpleServer simpleServer = new SimpleServer(config.getPort(), config.getDocRoot());
            RestHelper restHelper = new RestHelper(simpleServer);
            restHelper.addRestObject(new UserController())
                    .addRestObject(new CaptchaController());
            simpleServer.start(() -> {
                logger.info("service [ServiceUserMain] started.");
                logger.info(config);
            });
        }, new Stop(util));
    }

}