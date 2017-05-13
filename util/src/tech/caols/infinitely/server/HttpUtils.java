package tech.caols.infinitely.server;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.util.EntityUtils;

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

}
