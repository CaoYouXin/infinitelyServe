package tech.caols.infinitely.services;

import tech.caols.infinitely.viewmodels.ResourceFile;

import java.util.List;

public interface ResourceService {

    List<ResourceFile> listAllResource(String root);

    List<ResourceFile> createDir(String root, String path);

    List<ResourceFile> deleteAll(String root, String path);

    List<ResourceFile> copy(String srcRoot, List<String> srcPaths, String destRoot, String destPath);

}
