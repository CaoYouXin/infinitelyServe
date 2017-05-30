package tech.caols.infinitely.rest;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.consts.ConfigsKeys;
import tech.caols.infinitely.datamodels.Configs;
import tech.caols.infinitely.datamodels.RestRecord;
import tech.caols.infinitely.datamodels.Token;
import tech.caols.infinitely.repositories.ConfigsRepository;
import tech.caols.infinitely.repositories.RestRepository;
import tech.caols.infinitely.repositories.TokenRepository;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(RestHandler.class);

    private final Object object;
    private final Method method;
    private final RestAPI rest;

    private RestRepository restRepository = new RestRepository();
    private TokenRepository tokenRepository = new TokenRepository();
    private ConfigsRepository configsRepository = new ConfigsRepository();

    public RestHandler(Object object, Method method, RestAPI rest) {
        this.object = object;
        this.method = method;
        this.rest = rest;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

        if (!this.rest.target().name().equals(HttpUtils.getMethod(request))) {
            throw new MethodNotSupportedException("not a " + this.rest.target().name() + ". but a " + HttpUtils.getMethod(request));
        }

        if (this.rest.auth()) {
            String token = HttpUtils.getParameterMap(request).get("user_token");
            if (null == token) {
                HttpUtils.response(response, JsonRes.getFailJsonRes(Constants.CODE_UNAUTHED, null));
                return;
            }

            Token tokenByToken = this.tokenRepository.findTokenByToken(token);
            if (null == tokenByToken) {
                HttpUtils.response(response, JsonRes.getFailJsonRes(Constants.CODE_UNAUTHED, null));
                return;
            }

            if (this.rest.adminAuth()) {
                Configs configs = this.configsRepository.findByKey(ConfigsKeys.AdminUserId);
                if (Long.parseLong(configs.getValue()) != tokenByToken.getUserId()) {
                    HttpUtils.response(response, JsonRes.getFailJsonRes(Constants.CODE_UNPRIVILEGED, null));
                    return;
                }
            }
        }

        Object ret = null;
        try {

            List<Object> parameters = new ArrayList<>();
            for (Class<?> clazz : this.method.getParameterTypes()) {
                String typeName = clazz.getTypeName();
                switch (typeName) {
                    case "org.apache.http.HttpRequest":
                        parameters.add(request);
                        break;
                    case "org.apache.http.HttpResponse":
                        parameters.add(response);
                        break;
                    case "org.apache.http.protocol.HttpContext":
                        parameters.add(context);
                        break;
                    case "java.util.Map":
                        parameters.add(HttpUtils.getParameterMap(request));
                        break;
                    default:
                        parameters.add(HttpUtils.getBodyAsObject(request, clazz));
                        break;
                }
            }

            switch (parameters.size()) {
                case 0:
                    ret = this.method.invoke(this.object);
                    break;
                case 1:
                    ret = this.method.invoke(this.object, parameters.get(0));
                    break;
                case 2:
                    ret = this.method.invoke(this.object, parameters.get(0), parameters.get(1));
                    break;
                case 3:
                    ret = this.method.invoke(this.object, parameters.get(0), parameters.get(1), parameters.get(2));
                    break;
                case 4:
                    ret = this.method.invoke(this.object, parameters.get(0),
                            parameters.get(1), parameters.get(2), parameters.get(3));
                    break;
                case 5:
                    ret = this.method.invoke(this.object, parameters.get(0), parameters.get(1),
                            parameters.get(2), parameters.get(3), parameters.get(4));
                    break;
                default:
                    throw new RuntimeException("始料未及. parameter count = " + parameters.size());
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.catching(e);
        }

        if (ret != null) {
            HttpUtils.response(context, ret);
        }

        RestRecord restRecord = new RestRecord();
        restRecord.setRestName(this.rest.name());
        restRecord.setUrl(this.rest.url());
        restRecord.setRemoteIp(SimpleUtils.getIpFromRequest(request, context));
        restRecord.setCreate(new Date());
        this.restRepository.save(restRecord);
    }

}
