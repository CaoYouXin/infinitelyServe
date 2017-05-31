package tech.caols.infinitely.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.CallBacks;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.cmd.Stop;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.PostConfigs;
import tech.caols.infinitely.config.SimpleConfig;
import tech.caols.infinitely.register.PostRegister;
import tech.caols.infinitely.rest.RestHelper;
import tech.caols.infinitely.server.SimpleServer;

import java.net.UnknownHostException;
import java.util.List;

public class PostFavourMain {

    private static final Logger logger = LogManager.getLogger(PostFavourMain.class);

    public static void main(String[] args) {
        final ConfigUtil util = new ConfigUtil();
        final Config config = util.getConfigFromFile(util.getRootFileName() + ".json", Config.class);
        String hostName = null;
        try {
            hostName = SimpleUtils.getLocalHostLANAddress().getHostAddress();
        } catch (UnknownHostException e) {
            logger.catching(e);
        }

        CallBacks registers = new CallBacks();
        CallBacks unregisters = new CallBacks();
        for (PostConfigs postConfigs : config.getConfigs()) {
            registers.add(new PostRegister(postConfigs, hostName, config.getServer().getPort(), true));
            unregisters.add(new PostRegister(postConfigs, hostName, config.getServer().getPort(), false));
        }

        SimpleUtils.main(args, () -> {
            SimpleServer simpleServer = new SimpleServer(config.getServer().getPort(), config.getServer().getDocRoot());

            RestHelper restHelper = new RestHelper(simpleServer);

            simpleServer.start(registers.add(() -> {
                logger.info("service [PostFavourMain] started.");
                logger.info(config);
            }));
        }, unregisters.add(new Stop(util)));
    }

    static class Config {
        private SimpleConfig server;
        private List<PostConfigs> configs;

        public SimpleConfig getServer() {
            return server;
        }

        public void setServer(SimpleConfig server) {
            this.server = server;
        }

        public List<PostConfigs> getConfigs() {
            return configs;
        }

        public void setConfigs(List<PostConfigs> configs) {
            this.configs = configs;
        }
    }

}
