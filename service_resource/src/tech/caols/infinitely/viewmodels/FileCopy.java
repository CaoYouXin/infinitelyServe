package tech.caols.infinitely.viewmodels;

import java.util.List;

public class FileCopy {

    private List<String> srcPaths;
    private String destPath;

    public List<String> getSrcPaths() {
        return srcPaths;
    }

    public void setSrcPaths(List<String> srcPaths) {
        this.srcPaths = srcPaths;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }
}
