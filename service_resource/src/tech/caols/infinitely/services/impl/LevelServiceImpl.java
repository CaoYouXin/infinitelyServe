package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.consts.ConfigsKeys;
import tech.caols.infinitely.datamodels.Configs;
import tech.caols.infinitely.datamodels.LevelData;
import tech.caols.infinitely.repositories.ConfigsRepository;
import tech.caols.infinitely.repositories.LevelRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.LevelService;
import tech.caols.infinitely.viewmodels.LevelView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LevelServiceImpl implements LevelService {

    private LevelRepository levelRepository = new LevelRepository();
    private ConfigsRepository configsRepository = new ConfigsRepository();

    @Override
    public List<LevelView> list() {
        List<String> defaultUserLevel = new ArrayList<>();
        Configs configs = this.configsRepository.findByKey(ConfigsKeys.DefaultUserLevel);
        if (configs != null) {
            defaultUserLevel.add(0, configs.getValue());
        } else {
            defaultUserLevel.add(0, "");
        }

        return this.levelRepository.findAll().stream().map(levelData -> {
            LevelView levelView = new LevelView();
            BeanUtils.copyBean(levelData, levelView);
            levelView.setDefault(defaultUserLevel.get(0).equals(levelData.getName()));
            return levelView;
        }).collect(Collectors.toList());
    }

    @Override
    public List<LevelView> save(LevelView levelView, HttpResponse response) {
        LevelData levelData = new LevelData();
        BeanUtils.copyBean(levelView, levelData);
        if (!this.levelRepository.save(levelData)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("保存资源等级失败! "));
            return null;
        }

        if (levelView.isDefault()) {
            if (!this.configsRepository.save(ConfigsKeys.DefaultUserLevel, levelData.getName())) {
                HttpUtils.response(response, JsonRes.getFailJsonRes("设置默认用户等级失败! "));
                return null;
            }
        }

        return this.list();
    }

    @Override
    public List<LevelView> delete(List<Long> ids, HttpResponse response) {
        if (!this.levelRepository.deleteInBatch(ids)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("删除资源等级失败! "));
            return null;
        }

        return this.list();
    }

}
