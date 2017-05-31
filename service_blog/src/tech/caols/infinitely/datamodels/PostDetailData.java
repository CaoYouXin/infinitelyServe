package tech.caols.infinitely.datamodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
public class PostDetailData {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "create", nullable = false)
    private Date create;

    @Column(name = "update", nullable = false)
    private Date update;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "like")
    private int like;

    @Column(name = "platform", nullable = false, length = 45)
    private String platform;

    @Column(name = "type", nullable = false, length = 45)
    private String type;

    @Column(name = "script")
    private String script;

    @Column(name = "screenshot")
    private String screenshot;

    @Column(name = "brief")
    private String brief;

    @Column(name = "resource_level_id")
    private Long resourceLevelId;

    @Column(name = "resource_level_name")
    private String resourceLevelName;

    @Column(name = "disabled")
    private byte disabled;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public byte getDisabled() {
        return disabled;
    }

    public void setDisabled(byte disabled) {
        this.disabled = disabled;
    }

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
}
