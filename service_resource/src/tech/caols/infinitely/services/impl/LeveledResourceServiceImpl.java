package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.consts.ConfigsKeys;
import tech.caols.infinitely.datamodels.Configs;
import tech.caols.infinitely.datamodels.LeveledResourceData;
import tech.caols.infinitely.repositories.ConfigsRepository;
import tech.caols.infinitely.repositories.LeveledResourceDetailRepository;
import tech.caols.infinitely.repositories.LeveledResourceRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.LeveledResourceService;
import tech.caols.infinitely.viewmodels.LeveledResourceDetailView;
import tech.caols.infinitely.viewmodels.LeveledResourceView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LeveledResourceServiceImpl implements LeveledResourceService {

    private LeveledResourceRepository leveledResourceRepository = new LeveledResourceRepository();
    private LeveledResourceDetailRepository leveledResourceDetailRepository = new LeveledResourceDetailRepository();
    private ConfigsRepository configsRepository = new ConfigsRepository();

    @Override
    public List<LeveledResourceDetailView> findAll() {
        return this.leveledResourceDetailRepository.findAll().stream().map(leveledResourceViewData -> {
            LeveledResourceDetailView leveledResourceDetailView = new LeveledResourceDetailView();
            BeanUtils.copyBean(leveledResourceViewData, leveledResourceDetailView);
            return leveledResourceDetailView;
        }).collect(Collectors.toList());
    }

    @Override
    public List<LeveledResourceDetailView> save(LeveledResourceView leveledResourceView, HttpResponse response) {
        LeveledResourceData leveledResourceData = new LeveledResourceData();
        BeanUtils.copyBean(leveledResourceView, leveledResourceData);

        if (!this.leveledResourceRepository.save(leveledResourceData)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("保存资源目录失败"));
            return null;
        }

        this.saveUpdateDateTime();

        return this.findAll();
    }

    @Override
    public List<LeveledResourceDetailView> delete(List<Long> ids, HttpResponse response) {
        if (!this.leveledResourceRepository.deleteInBatch(ids)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes("删除资源目录失败"));
            return null;
        }

        this.saveUpdateDateTime();

        return this.findAll();
    }

    private void saveUpdateDateTime() {
        Configs configs = this.configsRepository.findByKey(ConfigsKeys.LastUpdateLeveledResource);
        if (null == configs) {
            configs = new Configs();
            configs.setKey(ConfigsKeys.LastUpdateLeveledResource);
        }

        configs.setValue(DateFormat.getInstance().format(new Date()));
        this.configsRepository.save(configs);
    }

}
