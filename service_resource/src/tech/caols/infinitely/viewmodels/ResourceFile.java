package tech.caols.infinitely.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class ResourceFile {

    private List<ResourceFile> contents;
    private String name;
    private boolean isDirectory;

    public ResourceFile(String name) {
        this.name = name;
    }

    public void AddContent(ResourceFile resourceFile) {
        if (this.contents == null) {
            this.contents = new ArrayList<>();
        }
        this.contents.add(resourceFile);
    }

    public List<ResourceFile> getContents() {
        return contents;
    }

    public void setContents(List<ResourceFile> contents) {
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner("\n", "[", "]");
        if (this.contents != null) {
            this.contents.forEach(content -> {
                stringJoiner.add(content.toString());
            });
        }
        return "ResourceFile{" +
                "contents=" + stringJoiner.toString() +
                ", name='" + name + '\'' +
                '}';
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }
}
