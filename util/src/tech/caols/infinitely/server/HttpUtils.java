package tech.caols.infinitely.server;

import org.apache.http.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    public static Map<String, String> getParameterMap(HttpRequest httpRequest) {
        String decodedUrl = getDecodedUrl(httpRequest);
        int indexOf = decodedUrl.indexOf('?');
        if (-1 == indexOf) {
            throw new RuntimeException("wrong config url pattern.");
        }

        Map<String, String> ret = new HashMap<>();
        putParameter(decodedUrl.substring(indexOf + 1), ret);
        return ret;
    }

    private static void putParameter(String str, Map<String, String> map) {
        int indexOf = str.indexOf(';'), indexOfEqual = str.indexOf('=');
        if (-1 != indexOf) {
            if (-1 != indexOfEqual) {
                map.put(str.substring(0, indexOfEqual),
                        str.substring(indexOfEqual + 1, indexOf));
                putParameter(str.substring(indexOf + 1), map);
            }
        } else {
            if (-1 != indexOfEqual) {
                map.put(str.substring(0, indexOfEqual),
                        str.substring(indexOfEqual + 1));
            }
        }
    }

}
