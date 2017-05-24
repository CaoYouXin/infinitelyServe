package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.LeveledResourceService;
import tech.caols.infinitely.services.impl.LeveledResourceServiceImpl;
import tech.caols.infinitely.viewmodels.Deletion;
import tech.caols.infinitely.viewmodels.LeveledResourceDetailView;
import tech.caols.infinitely.viewmodels.LeveledResourceView;

import java.util.List;

@Rest
public class LeveledResourceController {

    private LeveledResourceService leveledResourceService = new LeveledResourceServiceImpl();

    @RestAPI(name = "list_all_leveled_resource", url = "/leveled/resource/list", target = RestTarget.GET)
    public List<LeveledResourceDetailView> list() {
        return this.leveledResourceService.findAll();
    }

    @RestAPI(name = "list_all_leveled_resource", url = "/leveled/resource/save", target = RestTarget.POST)
    public List<LeveledResourceDetailView> save(LeveledResourceView leveledResourceView, HttpResponse response) {
        return this.leveledResourceService.save(leveledResourceView, response);
    }

    @RestAPI(name = "list_all_leveled_resource", url = "/leveled/resource/delete", target = RestTarget.POST)
    public List<LeveledResourceDetailView> delete(Deletion deletion, HttpResponse response) {
        return this.leveledResourceService.delete(deletion.getIds(), response);
    }

}
