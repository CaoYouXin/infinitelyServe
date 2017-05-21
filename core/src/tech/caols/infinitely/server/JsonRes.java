package tech.caols.infinitely.server;

import tech.caols.infinitely.Constants;

public class JsonRes<T> {

    public static JsonRes SuccessJsonRes = new JsonRes(Constants.CODE_VALID);

    public static JsonRes getSuccessJsonRes(String message) {
        return new JsonRes<>(Constants.CODE_VALID, message);
    }

    public static JsonRes getFailJsonRes(int code, String reason) {
        return new JsonRes<>(code, reason);
    }

    public static JsonRes getFailJsonRes(String reason) {
        return new JsonRes<>(Constants.CODE_INVALID, reason);
    }

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
