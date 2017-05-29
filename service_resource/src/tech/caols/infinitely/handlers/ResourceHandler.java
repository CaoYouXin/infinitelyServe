package tech.caols.infinitely.handlers;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.SimpleUtils;
import tech.caols.infinitely.consts.ConfigsKeys;
import tech.caols.infinitely.datamodels.Configs;
import tech.caols.infinitely.datamodels.LevelData;
import tech.caols.infinitely.datamodels.LeveledResourceData;
import tech.caols.infinitely.datamodels.RestRecord;
import tech.caols.infinitely.repositories.ConfigsRepository;
import tech.caols.infinitely.repositories.LevelRepository;
import tech.caols.infinitely.repositories.LeveledResourceRepository;
import tech.caols.infinitely.repositories.RestRepository;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.handlers.HttpFileHandler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ResourceHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(ResourceHandler.class);

    private ConfigsRepository configsRepository = new ConfigsRepository();
    private LeveledResourceRepository leveledResourceRepository = new LeveledResourceRepository();
    private LevelRepository levelRepository = new LevelRepository();
    private RestRepository restRepository = new RestRepository();

    private final HttpRequestHandler handler;
    private final String docRoot;
    private final String urlRoot;

    private Date lastUpdate;
    private List<LeveledResourceData> leveledResourceDataList;
    private String unPermissionedMsg;

    public ResourceHandler(String docRoot, String urlRoot) {
        this.docRoot = docRoot;
        this.urlRoot = urlRoot;
        this.handler = new HttpFileHandler(docRoot, urlRoot);
    }

    private boolean needFetchData() {
        Configs configs = this.configsRepository.findByKey(ConfigsKeys.LastUpdateLeveledResource);
        if (null == configs) {
            return false;
        }

        Date parse = null;
        try {
            parse = DateFormat.getInstance().parse(configs.getValue());

        } catch (ParseException e) {
            logger.catching(e);
        }

        if (null == parse) {
            throw new RuntimeException("configs in database are not well formatted.");
        }

        if (null == this.lastUpdate) {
            this.lastUpdate = parse;
            return true;
        }

        if (parse.after(this.lastUpdate)) {
            this.lastUpdate = parse;
            return true;
        }
        return false;
    }

    private void fetchData() {
        this.leveledResourceDataList = this.leveledResourceRepository.findAll();
    }

    private boolean checkPermission(String url, HttpContext context) {
        if (null == this.leveledResourceDataList) {
            return true;
        }

        final List<Integer> len = new ArrayList<>();
        len.add(-1);
        final List<Long> levelId = new ArrayList<>();
        levelId.add(null);
        this.leveledResourceDataList.forEach(leveledResourceData -> {
            String name = leveledResourceData.getName();
            if (url.startsWith(name)) {
                if (name.length() > len.get(0)) {
                    len.set(0, name.length());
                    levelId.set(0, leveledResourceData.getLevelId());
                }
            }
        });

        if (levelId.get(0) == null) {
            return true;
        }

        LevelData levelData = this.levelRepository.find(levelId.get(0));

        Object userLevels = context.getAttribute(Constants.USER_LEVELS);
        if (null == userLevels) {
            this.unPermissionedMsg = levelData.getMsg();
            return false;
        }

        if (Arrays.asList(userLevels.toString().split(",")).contains(levelData.getName())) {
            return true;
        }
        this.unPermissionedMsg = levelData.getMsg();
        return false;
    }

    private String getMsg() {
        return this.unPermissionedMsg;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        if (this.needFetchData()) {
            this.fetchData();
        }

        String decodedUrl = HttpUtils.getDecodedUrl(request);
        if (!this.checkPermission(decodedUrl.substring(this.urlRoot.length()), context)) {
            response.setStatusCode(HttpStatus.SC_FORBIDDEN);
            StringEntity entity = new StringEntity(
                    String.format("<html><body><h1>Access denied</h1><p>%s</p></body></html>", this.getMsg()),
                    ContentType.create("text/html", "UTF-8"));
            response.setEntity(entity);
            logger.error("Cannot serve " + decodedUrl);
            return;
        }

        this.handler.handle(request, response, context);

        RestRecord restRecord = new RestRecord();
        restRecord.setRestName("RESOURCE SERVING");
        restRecord.setUrl(decodedUrl);
        restRecord.setRemoteIp(SimpleUtils.getIpFromRequest(request, context));
        restRecord.setCreate(new Date());
        this.restRepository.save(restRecord);
    }

}
