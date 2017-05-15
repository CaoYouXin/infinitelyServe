package tech.caols.infinitely.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import tech.caols.infinitely.Constants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class HttpUtils {

    public static String getMethod(HttpRequest httpRequest) {
        return httpRequest.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
    }

    public static boolean isGet(HttpRequest httpRequest) {
        return getMethod(httpRequest).equals("GET");
    }

    public static boolean isPost(HttpRequest httpRequest) {
        return getMethod(httpRequest).equals("POST");
    }

    public static boolean isInvalidMethod(HttpRequest httpRequest) {
        String method = getMethod(httpRequest);
        return !method.equals("GET") && !method.equals("HEAD") && !method.equals("POST");
    }

    public static String getDecodedUrl(HttpRequest httpRequest) {
        try {
            return URLDecoder.decode(httpRequest.getRequestLine().getUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
        String decodedUrl = getDecodedUrl(httpRequest);
        int indexOf = decodedUrl.indexOf('?');
        if (-1 == indexOf) {
            throw new RuntimeException("wrong config url pattern.");
        }

        Map<String, String> ret = new HashMap<>();
        putParameter(decodedUrl.substring(indexOf + 1), ret, ";");
        return ret;
    }

    public static void putParameter(String str, Map<String, String> map, String delimiter) {
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

    public static List<String> getListParameter(String param) {
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
                e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    public static void response(HttpResponse httpResponse, JsonRes jsonRes) {
        httpResponse.setStatusCode(HttpStatus.SC_OK);
        try {
            httpResponse.setEntity(new StringEntity(
                    new ObjectMapper().writeValueAsString(jsonRes),
                    ContentType.APPLICATION_JSON
            ));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            httpResponse.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static void response(HttpContext httpContext, Object jsonResBody) {
        httpContext.setAttribute(Constants.RET_OBJECT, jsonResBody);
    }

}
