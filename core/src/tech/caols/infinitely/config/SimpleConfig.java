package tech.caols.infinitely.config;

public class SimpleConfig {

    private String docRoot;
    private Integer port;

    public String getDocRoot() {
        return docRoot;
    }

    public void setDocRoot(String docRoot) {
        this.docRoot = docRoot;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "SimpleConfig{" +
                "docRoot='" + docRoot + '\'' +
                ", port=" + port +
                '}';
    }
}
