package tech.caols.infinitely.server;

import java.util.Map;

public class PreReq {

    private Map<String, String> parameters;
    private String body;

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}