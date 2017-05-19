package tech.caols.infinitely.server;

import org.apache.http.HttpHost;
import org.apache.http.impl.pool.BasicConnPool;
import org.apache.http.impl.pool.BasicPoolEntry;
import org.apache.http.pool.PoolStats;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SimplePool {

    private static final Logger logger = LogManager.getLogger(SimplePool.class);

    private static final SimplePool _self = new SimplePool();

    public static SimplePool get() {
        return _self;
    }

    private BasicConnPool pool;

    private SimplePool() {
        this.pool = new BasicConnPool();
        this.pool.setMaxTotal(20);
        this.pool.setDefaultMaxPerRoute(2);
    }

    public BasicPoolEntry getConn(HttpHost target) {
        Future<BasicPoolEntry> future = this.pool.lease(target, null);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.catching(e);
        }
        return null;
    }

    public void release(BasicPoolEntry entry) {
        this.pool.release(entry, true);

        PoolStats totalStats = this.pool.getTotalStats();
        if (totalStats.getMax() >= 20 * 0.7) {
            if (totalStats.getAvailable() <= totalStats.getMax() * 0.2) {
                this.pool.closeExpired();
                this.pool.closeIdle(1, TimeUnit.MINUTES);
            }
        }
    }

    public void shutdown() {
        try {
            this.pool.shutdown();
        } catch (IOException e) {
            logger.catching(e);
        }
    }

}
