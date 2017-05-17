package tech.caols.infinitely.cmd;

import tech.caols.infinitely.CallBack;
import tech.caols.infinitely.config.ConfigUtil;
import tech.caols.infinitely.config.ShutDownConfig;
import tech.caols.infinitely.server.Stopper;

public class Stop implements CallBack {

    private final ConfigUtil util;

    public Stop(ConfigUtil util) {
        this.util = util;
    }

    @Override
    public void call() {
        String fileName = this.util.getRootFileName() + ".log";

        ShutDownConfig shutDownConfig = this.util.getConfigFromFile(fileName, ShutDownConfig.class);
        new Stopper(shutDownConfig.getHostName(), shutDownConfig.getHostPort(), shutDownConfig.getToken()).call();

        this.util.eraseConfigFile(fileName);
    }
}
