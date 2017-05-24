package tech.caols.infinitely.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.caols.infinitely.services.ResourceService;
import tech.caols.infinitely.util.FileDeleter;
import tech.caols.infinitely.util.FileLister;
import tech.caols.infinitely.viewmodels.ResourceFile;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ResourceServiceImpl implements ResourceService {

    private static final Logger logger = LogManager.getLogger(ResourceServiceImpl.class);

    @Override
    public List<ResourceFile> listAllResource(String root) {
        File rootFile = new File(root);
        if (!rootFile.exists()) {
            return null;
        }

        return new FileLister(rootFile).list();
    }

    @Override
    public List<ResourceFile> createDir(String root, String path) {
        File file = new File(root + path.substring(1));
        if (!file.mkdirs()) {
            return null;
        }

        return this.listAllResource(root);
    }

    @Override
    public List<ResourceFile> deleteAll(String root, String path) {
        File file = new File(root + path.substring(1));
        if (!new FileDeleter(file).call()) {
            return null;
        }

        return this.listAllResource(root);
    }

    @Override
    public List<ResourceFile> copy(String srcRoot, List<String> srcPaths, String destRoot, String destPath) {
        srcPaths.forEach(srcPath -> {
            File srcFile = new File(srcRoot + srcPath.substring(1));
            if (!srcFile.exists()) {
                return;
            }

            File dstFile = new File(destRoot + destPath + '/' + srcFile.getName());

            try {
                Files.copy(srcFile.toPath(), dstFile.toPath(), REPLACE_EXISTING);
            } catch (IOException e) {
                logger.catching(e);
            }
        });

        return this.listAllResource(destRoot);
    }

}
