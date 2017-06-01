package tech.caols.infinitely.datamodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
@Table(name = "category")
public class CategoryData {

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

    @Column(name = "script", nullable = false)
    private String script;

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

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public byte getDisabled() {
        return disabled;
    }

    public void setDisabled(byte disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance();
        return "CategoryData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", create=" + dateTimeInstance.format(create) +
                ", update=" + dateTimeInstance.format(update) +
                ", url='" + url + '\'' +
                ", script='" + script + '\'' +
                ", disabled=" + disabled +
                '}';
    }
}
