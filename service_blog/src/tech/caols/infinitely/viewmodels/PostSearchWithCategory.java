package tech.caols.infinitely.viewmodels;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class PostSearchWithCategory {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date categoryStart;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date categoryEnd;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date postStart;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date postEnd;

    private List<String> categoryKeywords;
    private List<String> postKeywords;
    private String platform;

    public Date getCategoryStart() {
        return categoryStart;
    }

    public void setCategoryStart(Date categoryStart) {
        this.categoryStart = categoryStart;
    }

    public Date getCategoryEnd() {
        return categoryEnd;
    }

    public void setCategoryEnd(Date categoryEnd) {
        this.categoryEnd = categoryEnd;
    }

    public Date getPostStart() {
        return postStart;
    }

    public void setPostStart(Date postStart) {
        this.postStart = postStart;
    }

    public Date getPostEnd() {
        return postEnd;
    }

    public void setPostEnd(Date postEnd) {
        this.postEnd = postEnd;
    }

    public List<String> getCategoryKeywords() {
        return categoryKeywords;
    }

    public void setCategoryKeywords(List<String> categoryKeywords) {
        this.categoryKeywords = categoryKeywords;
    }

    public List<String> getPostKeywords() {
        return postKeywords;
    }

    public void setPostKeywords(List<String> postKeywords) {
        this.postKeywords = postKeywords;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
