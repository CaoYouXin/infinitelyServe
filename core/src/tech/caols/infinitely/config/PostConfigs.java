package tech.caols.infinitely.config;

import java.util.List;

public class PostConfigs {

    private String hostName;
    private int port;
    private List<PostConfigItem> items;

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

    public List<PostConfigItem> getItems() {
        return items;
    }

    public void setItems(List<PostConfigItem> items) {
        this.items = items;
    }

    public static class PostConfigItem {

        private String regex;
        private String url;
        private List<String> parameters;
        private boolean isNeedBody;
        private boolean isNeedRetObj;
        private boolean isNeedUrl;

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

        public boolean isNeedRetObj() {
            return isNeedRetObj;
        }

        public void setNeedRetObj(boolean needRetObj) {
            isNeedRetObj = needRetObj;
        }

        public boolean isNeedUrl() {
            return isNeedUrl;
        }

        public void setNeedUrl(boolean needUrl) {
            isNeedUrl = needUrl;
        }
    }

}
