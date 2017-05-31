package tech.caols.infinitely.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.http.HttpHost;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PostConfig {

    @JsonProperty("hostname")
    private String hostName;

    @JsonProperty("port")
    private int port;

    @JsonProperty("parameters")
    private List<String> parameters;

    @JsonProperty("isNeedBody")
    private boolean isNeedBody;

    @JsonProperty("isNeedRetObj")
    private boolean isNeedRetObj;

    @JsonProperty("regex")
    private String regexStr;

    @JsonProperty("url")
    private String url;

    @JsonIgnore
    private HttpHost host;
    @JsonIgnore
    private Pattern regex;

    public PostConfig() {
    }

    @JsonIgnore
    public HttpHost getHost() {
        if (this.host == null) {
            this.host = new HttpHost(this.hostName, this.port);
        }
        return this.host;
    }

    @JsonIgnore
    public List<String> getParameters() {
        return parameters;
    }

    @JsonIgnore
    public boolean isNeedBody() {
        return isNeedBody;
    }

    @JsonIgnore
    public boolean isNeedRetObj() {
        return isNeedRetObj;
    }

    @JsonIgnore
    public Pattern getRegex() {
        if (this.regex == null) {
            this.regex = Pattern.compile(this.regexStr);
        }
        return this.regex;
    }

    @JsonIgnore
    public String getUrl() {
        return url;
    }

    @JsonIgnore
    public String getRegexStr() {
        return regexStr;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public void setNeedBody(boolean needBody) {
        isNeedBody = needBody;
    }

    public void setNeedRetObj(boolean needRetObj) {
        isNeedRetObj = needRetObj;
    }

    public void setRegexStr(String regexStr) {
        this.regexStr = regexStr;
    }

    @Override
    public String toString() {
        return "PostConfig{" +
                "hostName='" + hostName + '\'' +
                ", port=" + port +
                ", parameters=" + parameters +
                ", isNeedBody=" + isNeedBody +
                ", isNeedRetObj=" + isNeedRetObj +
                ", regexStr='" + regexStr + '\'' +
                ", host=" + this.getHost() +
                ", regex=" + this.getRegex() +
                '}';
    }

    private static ConcurrentSkipListSet<PostConfig> PostConfigList = new ConcurrentSkipListSet<>();

    public static Iterable<PostConfig> match(String url) {
        return PostConfigList.stream().filter(preConfig -> preConfig.getRegex().matcher(url).matches()).collect(Collectors.toList());
    }

    public static boolean addConfig(PostConfig postConfig) {
        return PostConfigList.add(postConfig);
    }

    public static boolean removeConfig(PostConfig postConfig) {
        for (PostConfig config : PostConfigList) {
            if (config.getRegexStr().equals(postConfig.getRegexStr())) {
                return PostConfigList.remove(config);
            }
        }
        return false;
    }
}
