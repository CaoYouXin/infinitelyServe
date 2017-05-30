package tech.caols.infinitely.controllers;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.FavourResourceMapService;
import tech.caols.infinitely.services.impl.FavourResourceMapServiceImpl;
import tech.caols.infinitely.viewmodels.Deletion;
import tech.caols.infinitely.viewmodels.FavourResourceMapView;

import java.util.List;

@Rest
public class FavourResourceMapController {

    private FavourResourceMapService favourResourceMapService = new FavourResourceMapServiceImpl();

    @RestAPI(name = "list_all_fr_map", url = "/frmap/list", target = RestTarget.GET)
    public List<FavourResourceMapView> list() {
        return this.favourResourceMapService.list();
    }

    @RestAPI(name = "save_fr_map", url = "/frmap/save", target = RestTarget.POST)
    public List<FavourResourceMapView> save(FavourResourceMapView favourResourceMapView, HttpResponse response) {
        return this.favourResourceMapService.save(favourResourceMapView, response);
    }

    @RestAPI(name = "delete_fr_map", url = "/frmap/delete", target = RestTarget.POST)
    public List<FavourResourceMapView> delete(Deletion deletion, HttpResponse response) {
        return this.favourResourceMapService.delete(deletion.getIds(), response);
    }

}
