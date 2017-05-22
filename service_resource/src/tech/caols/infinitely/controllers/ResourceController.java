package tech.caols.infinitely.controllers;

import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.ResourceService;
import tech.caols.infinitely.services.impl.ResourceServiceImpl;
import tech.caols.infinitely.viewmodels.ResourceFile;

import java.util.List;

@Rest
public class ResourceController {

    private ResourceService resourceService = new ResourceServiceImpl();

    private final String sourceRoot;

    public ResourceController(String sourceRoot) {
        this.sourceRoot = sourceRoot;
    }

    @RestAPI(name = "list_all_sources", url = "/list/source", target = RestTarget.GET)
    public List<ResourceFile> listAllSources() {
        return this.resourceService.listAllSource(this.sourceRoot);
    }

}
