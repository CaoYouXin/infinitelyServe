package tech.caols.infinitely.util;

import java.io.File;

public class FileDeleter {

    private final File directory;

    public FileDeleter(File directory) {
        this.directory = directory;
    }

    public boolean call() {
        return this.delete(this.directory);
    }

    private boolean delete(File dir) {
        if (!dir.exists()) {
            return true;
        }


        File[] files = dir.listFiles();
        if (null == files) {
            return dir.delete();
        }

        boolean ret = true;
        for (File file : files) {
            if (file.isDirectory()) {
                this.delete(file);
            } else {
                ret = ret && file.delete();
            }
        }
        return ret && dir.delete();
    }
}
