package tech.caols.infinitely.server;

public class JsonRes<T> {

    private int code;
    private T body;

    public JsonRes(int code) {
        this.code = code;
    }

    public JsonRes(int code, T body) {
        this.code = code;
        this.body = body;
    }

    @Override
    public String toString() {
        return "JsonRes{" +
                "code=" + code +
                ", body=" + body +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
