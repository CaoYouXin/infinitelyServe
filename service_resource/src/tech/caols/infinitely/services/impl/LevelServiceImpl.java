package tech.caols.infinitely.services.impl;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.datamodels.LevelData;
import tech.caols.infinitely.repositories.LevelRepository;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;
import tech.caols.infinitely.services.LevelService;
import tech.caols.infinitely.viewmodels.LevelView;

import java.util.List;
import java.util.stream.Collectors;

public class LevelServiceImpl implements LevelService {

    private LevelRepository levelRepository = new LevelRepository();

    @Override
    public List<LevelView> list() {
        return this.levelRepository.findAll().stream().map(levelData -> {
            LevelView levelView = new LevelView();
            BeanUtils.copyBean(levelData, levelView);
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
