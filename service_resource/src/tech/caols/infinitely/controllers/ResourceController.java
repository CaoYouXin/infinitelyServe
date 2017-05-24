package tech.caols.infinitely.controllers;

import tech.caols.infinitely.rest.Rest;
import tech.caols.infinitely.rest.RestAPI;
import tech.caols.infinitely.rest.RestTarget;
import tech.caols.infinitely.services.ResourceService;
import tech.caols.infinitely.services.impl.ResourceServiceImpl;
import tech.caols.infinitely.viewmodels.FileCopy;
import tech.caols.infinitely.viewmodels.ResourceFile;

import java.util.List;
import java.util.Map;

@Rest
public class ResourceController {

    private ResourceService resourceService = new ResourceServiceImpl();

    private final String sourceRoot;
    private final String docRoot;

    public ResourceController(String sourceRoot, String docRoot) {
        this.sourceRoot = sourceRoot;
        this.docRoot = docRoot;
    }

    @RestAPI(name = "list_all_sources", url = "/list/source", target = RestTarget.GET)
    public List<ResourceFile> listAllSources() {
        return this.resourceService.listAllResource(this.sourceRoot);
    }

    @RestAPI(name = "list_all_resource", url = "/list/resource", target = RestTarget.GET)
    public List<ResourceFile> listAllResources() {
        return this.resourceService.listAllResource(this.docRoot);
    }

    @RestAPI(name = "add_dir", url = "/file/mkdir", target = RestTarget.GET)
    public List<ResourceFile> mkdir(Map<String, String> parameters) {
        return this.resourceService.createDir(this.docRoot, parameters.get("path"));
    }

    @RestAPI(name = "delete_file", url = "/file/delete", target = RestTarget.GET)
    public List<ResourceFile> delete(Map<String, String> parameters) {
        return this.resourceService.deleteAll(this.docRoot, parameters.get("path"));
    }

    @RestAPI(name = "copy_file", url = "/file/copy", target = RestTarget.POST)
    public List<ResourceFile> copy(FileCopy fileCopy) {
        return this.resourceService.copy(this.sourceRoot, fileCopy.getSrcPaths(), this.docRoot, fileCopy.getDestPath());
    }

}
