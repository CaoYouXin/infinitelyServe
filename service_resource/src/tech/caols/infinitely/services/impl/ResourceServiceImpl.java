package tech.caols.infinitely.services.impl;

import tech.caols.infinitely.services.ResourceService;
import tech.caols.infinitely.util.FileLister;
import tech.caols.infinitely.viewmodels.ResourceFile;

import java.io.File;
import java.util.List;

public class ResourceServiceImpl implements ResourceService {

    @Override
    public List<ResourceFile> listAllSource(String root) {
        File rootFile = new File(root);
        if (!rootFile.exists()) {
            return null;
        }

        return new FileLister(rootFile).list();
    }

}
