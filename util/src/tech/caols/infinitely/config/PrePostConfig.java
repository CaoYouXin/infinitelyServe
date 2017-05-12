package tech.caols.infinitely.config;

import org.apache.http.HttpHost;

import java.util.List;
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

    private Map<String, Config> data = new ConcurrentHashMap<>();

    public void setHost(String url, String type, HttpHost host) {
        Config config = this.data.get(url + type);
        if (null == config) {
            config = new Config();
        }
        config.setHost(host);
        this.data.put(url + type, config);
    }

    public HttpHost getHost(String url, String type) {
        Config config = this.data.get(url + type);
        if (null == config) {
            return null;
        }
        return config.getHost();
    }

    public void setNeedBody(String url, String type, boolean needBody) {
        Config config = this.data.get(url + type);
        if (null == config) {
            config = new Config();
        }
        config.setNeedBody(needBody);
        this.data.put(url + type, config);
    }

    public Boolean getNeedBody(String url, String type) {
        Config config = this.data.get(url + type);
        if (null == config) {
            return null;
        }
        return config.isNeedBody();
    }

    public void setParameters(String url, String type, List<String> parameters) {
        Config config = this.data.get(url + type);
        if (null == config) {
            config = new Config();
        }
        config.setParameters(parameters);
        this.data.put(url + type, config);
    }

    public List<String> getParameters(String url, String type) {
        Config config = this.data.get(url + type);
        if (null == config) {
            return null;
        }
        return config.getParameters();
    }

    public Config getConfig(String url, String type) {
        return this.data.get(url + type);
    }

    @Override
    public String toString() {
        Set<String> strings = this.data.keySet();
        StringJoiner keys = new StringJoiner(", ", "[", "]");
        for (String str: strings) {
            keys.add(str + " => " + this.data.get(str).toString());
        }
        return "PrePostConfig{" +
                "data=" + keys.toString() +
                '}';
    }

    public static class Config {
        private HttpHost host;
        private List<String> parameters;
        private Boolean needBody;

        public HttpHost getHost() {
            return host;
        }

        public void setHost(HttpHost host) {
            this.host = host;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public void setParameters(List<String> parameters) {
            this.parameters = parameters;
        }

        public Boolean isNeedBody() {
            return needBody;
        }

        public void setNeedBody(boolean needBody) {
            this.needBody = needBody;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "host=" + host.getHostName() +
                    ":" + host.getPort() +
                    ", parameters=" + parameters +
                    ", needBody=" + needBody +
                    '}';
        }
    }

}
