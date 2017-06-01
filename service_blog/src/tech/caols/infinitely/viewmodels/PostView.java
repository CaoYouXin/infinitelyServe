package tech.caols.infinitely.viewmodels;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import java.util.Date;

public class PostView {

    private Long id;
    private String name;

    private Date create;

    private Date update;

    private String url;

    private Long categoryId;
    private String categoryName;

    private int like;
    private String platform;

    private String type;
    private String script;
    private String brief;
    private String screenshot;

    private Long resourceLevelId;
    private String resourceLevelName;

    public Long getResourceLevelId() {
        return resourceLevelId;
    }

    public void setResourceLevelId(Long resourceLevelId) {
        this.resourceLevelId = resourceLevelId;
    }

    public String getResourceLevelName() {
        return resourceLevelName;
    }

    public void setResourceLevelName(String resourceLevelName) {
        this.resourceLevelName = resourceLevelName;
    }

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

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
