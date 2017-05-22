package tech.caols.infinitely.services;

import tech.caols.infinitely.viewmodels.ResourceFile;

import java.util.List;

public interface ResourceService {

    List<ResourceFile> listAllSource(String root);

}
