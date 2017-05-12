package tech.caols.infinitely.config;

import org.apache.http.HttpHost;

import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

public class PrePostConfig {

    private static final PrePostConfig _self = new PrePostConfig();

    public static PrePostConfig get() {
        return _self;
    }

    private PrePostConfig() {}

    private Map<String, HttpHost> data = new ConcurrentHashMap<>();

    public void setHost(String url, String type, HttpHost host) {
        this.data.put(url + type, host);
    }

    public HttpHost getHost(String url, String type) {
        return this.data.get(url + type);
    }

    @Override
    public String toString() {
        Set<String> strings = this.data.keySet();
        StringJoiner keys = new StringJoiner(", ", "[", "]");
        for (String str: strings) {
            HttpHost httpHost = this.data.get(str);
            keys.add(str + " => " + httpHost.getHostName() + ":" + httpHost.getPort());
        }
        return "PrePostConfig{" +
                "data=" + keys.toString() +
                '}';
    }
}
