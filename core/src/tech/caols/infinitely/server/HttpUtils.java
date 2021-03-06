package tech.caols.infinitely.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.Constants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpUtils {

    private static final Logger logger = LogManager.getLogger(HttpUtils.class);

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        OBJECT_MAPPER.setDateFormat(dateFormat);
    }

    public static String getMethod(HttpRequest httpRequest) {
        return httpRequest.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
    }

    public static boolean isGet(HttpRequest httpRequest) {
        return getMethod(httpRequest).equals("GET");
    }

    public static boolean isPost(HttpRequest httpRequest) {
        return getMethod(httpRequest).equals("POST");
    }

    public static boolean isOptions(HttpRequest httpRequest) {
        return getMethod(httpRequest).equals("OPTIONS");
    }

    public static boolean isInvalidMethod(HttpRequest httpRequest) {
        String method = getMethod(httpRequest);
        return !method.equals("GET") && !method.equals("HEAD") && !method.equals("POST");
    }

    public static String getDecodedUrl(HttpRequest httpRequest) {
        try {
            return URLDecoder.decode(httpRequest.getRequestLine().getUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.catching(e);
            return "";
        }
    }

    public static String getSimpleDecodedUrl(HttpRequest httpRequest) {
        String decodedUrl = getDecodedUrl(httpRequest);
        int indexOf = decodedUrl.indexOf('?');
        if (-1 == indexOf) {
            return decodedUrl;
        } else {
            return decodedUrl.substring(0, indexOf);
        }
    }

    public static Map<String, String> getParameterMap(HttpRequest httpRequest) {
        Map<String, String> ret = new HashMap<>();

        String decodedUrl = getDecodedUrl(httpRequest);
        int indexOf = decodedUrl.indexOf('?');
        if (-1 == indexOf) {
            return ret;
        }

        putParameter(decodedUrl.substring(indexOf + 1), ret, "&");
        return ret;
    }

    private static void putParameter(String str, Map<String, String> map, String delimiter) {
        int indexOf = str.indexOf(delimiter), indexOfEqual = str.indexOf('=');
        if (-1 != indexOf) {
            if (-1 != indexOfEqual) {
                map.put(str.substring(0, indexOfEqual),
                        str.substring(indexOfEqual + 1, indexOf));
                putParameter(str.substring(indexOf + delimiter.length()), map, delimiter);
            }
        } else {
            if (-1 != indexOfEqual) {
                map.put(str.substring(0, indexOfEqual),
                        str.substring(indexOfEqual + 1));
            }
        }
    }

    public static List<String> getParameterList(String param) {
        List<String> ret = new ArrayList<>();
        int indexOf = param.indexOf(',');
        putParameter(param, ret, indexOf);
        return ret;
    }

    private static void putParameter(String str, List<String> list, int indexOf) {
        if (-1 == indexOf) {
            list.add(str);
        } else {
            list.add(str.substring(0, indexOf));
            String next = str.substring(indexOf + 1);
            putParameter(next, list, next.indexOf(','));
        }
    }

    public static HttpEntity getBody(HttpRequest httpRequest) {
        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            return ((HttpEntityEnclosingRequest) httpRequest).getEntity();
        }
        return null;
    }

    public static String getBodyAsString(HttpRequest httpRequest) {
        HttpEntity entity = getBody(httpRequest);
        if (null != entity) {
            try {
                return EntityUtils.toString(entity, Consts.UTF_8);
            } catch (IOException e) {
                logger.catching(e);
            }
        }
        return "";
    }

    public static byte[] getBodyAsBytes(HttpRequest httpRequest) {
        HttpEntity entity = getBody(httpRequest);
        if (null != entity) {
            try {
                return EntityUtils.toByteArray(entity);
            } catch (IOException e) {
                logger.catching(e);
            }
        }
        return new byte[0];
    }

    public static <T> T getBodyAsObject(HttpRequest httpRequest, Class<T> clazz) {
        String bodyAsString = getBodyAsString(httpRequest);
        try {
            return OBJECT_MAPPER.readValue(bodyAsString, clazz);
        } catch (IOException e) {
            logger.catching(e);
            throw new RuntimeException(e);
        }
    }

    public static void response(HttpResponse httpResponse, String jsonString) {
        httpResponse.setStatusCode(HttpStatus.SC_OK);
        logger.info("returning --->" + jsonString);
        httpResponse.setEntity(new StringEntity(
                jsonString,
                ContentType.APPLICATION_JSON
        ));
    }

    public static void response(HttpResponse httpResponse, JsonRes jsonRes) {
        httpResponse.setStatusCode(HttpStatus.SC_OK);
        try {
            String retString = OBJECT_MAPPER.writeValueAsString(jsonRes);
            response(httpResponse, retString);
        } catch (JsonProcessingException e) {
            logger.catching(e);
            httpResponse.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static void response(HttpContext httpContext, Object jsonResBody) {
        httpContext.setAttribute(Constants.RET_OBJECT, jsonResBody);
    }

}
