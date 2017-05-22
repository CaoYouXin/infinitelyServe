package tech.caols.infinitely.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class ResourceFile {

    @JsonProperty("contents")
    private List<ResourceFile> contents;
    @JsonProperty("name")
    private String name;

    public ResourceFile(String name) {
        this.name = name;
    }

    public void AddContent(ResourceFile resourceFile) {
        if (this.contents == null) {
            this.contents = new ArrayList<>();
        }
        this.contents.add(resourceFile);
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
}
