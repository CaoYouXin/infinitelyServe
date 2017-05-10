package tech.caols.infinitely.config;

public class ShutDownConfig {

    private String hostName;
    private Integer hostPort;
    private String token;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getHostPort() {
        return hostPort;
    }

    public void setHostPort(Integer hostPort) {
        this.hostPort = hostPort;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ShutDownConfig{" +
                "hostName='" + hostName + '\'' +
                ", hostPort=" + hostPort +
                ", token='" + token + '\'' +
                '}';
    }
}
