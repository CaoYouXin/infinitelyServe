package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.LevelService;
import tech.caols.infinitely.services.impl.LevelServiceImpl;
import tech.caols.infinitely.viewmodels.Deletion;
import tech.caols.infinitely.viewmodels.LevelView;

import java.util.List;

@Rest
public class LevelController {

    private LevelService levelService = new LevelServiceImpl();

    @RestAPI(name = "list_all_levels", url = "/level/list", target = RestTarget.GET)
    public List<LevelView> list() {
        return this.levelService.list();
    }

    @RestAPI(name = "save_level", url = "/level/save", target = RestTarget.POST)
    public List<LevelView> save(LevelView levelView, HttpResponse response) {
        return this.levelService.save(levelView, response);
    }

    @RestAPI(name = "delete_level", url = "/level/delete", target = RestTarget.POST)
    public List<LevelView> delete(Deletion deletion, HttpResponse response) {
        return this.levelService.delete(deletion.getIds(), response);
    }

}
