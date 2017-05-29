package tech.caols.infinitely.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PreConfigs {

    private String hostName;
    private int port;
    private List<PreConfigItem> items;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<PreConfigItem> getItems() {
        return items;
    }

    public void setItems(List<PreConfigItem> items) {
        this.items = items;
    }

    public static class PreConfigItem {

        private String regex;
        private String url;
        private List<String> parameters;
        private boolean isNeedBody;

        public String getRegex() {
            return regex;
        }

        public void setRegex(String regex) {
            this.regex = regex;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public void setParameters(List<String> parameters) {
            this.parameters = parameters;
        }

        public boolean isNeedBody() {
            return isNeedBody;
        }

        public void setNeedBody(boolean needBody) {
            isNeedBody = needBody;
        }
    }
}
