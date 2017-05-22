package tech.caols.infinitely.util;

import tech.caols.infinitely.viewmodels.ResourceFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileLister {

    private File root;

    public FileLister(File root) {
        this.root = root;
    }

    public List<ResourceFile> list() {
        List<ResourceFile> ret = new ArrayList<>();
        this.list(this.root, ret);
        ret.forEach(System.out::println);
        return ret;
    }

    private void list(File dir, List<ResourceFile> list) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            ResourceFile resourceFile = new ResourceFile(file.getName());
            list.add(resourceFile);
            if (file.isDirectory()) {
                List<ResourceFile> ret = new ArrayList<>();
                this.list(file, ret);
                ret.forEach(resourceFile::AddContent);
            }
        }
    }

}
