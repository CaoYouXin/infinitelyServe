package tech.caols.infinitely.viewmodels;

public class CommentRequest {

    private Long idWhatEver;
    private String userName;
    private String atUserName;
    private String content;

    public Long getIdWhatEver() {
        return idWhatEver;
    }

    public void setIdWhatEver(Long idWhatEver) {
        this.idWhatEver = idWhatEver;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAtUserName() {
        return atUserName;
    }

    public void setAtUserName(String atUserName) {
        this.atUserName = atUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
