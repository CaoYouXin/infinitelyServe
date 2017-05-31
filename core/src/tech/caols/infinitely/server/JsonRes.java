package tech.caols.infinitely.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.Constants;

public class JsonRes<T> {

    private static final Logger logger = LogManager.getLogger(JsonRes.class);

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
        try {
            return HttpUtils.OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.catching(e);
        }
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
