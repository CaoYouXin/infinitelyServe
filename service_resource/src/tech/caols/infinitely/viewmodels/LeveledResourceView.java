package tech.caols.infinitely.viewmodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

public class LeveledResourceView {

    private Long id;
    private String name;
    private Long levelId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }
}
