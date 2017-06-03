package tech.caols.infinitely.viewmodels;

public class PostSearchWithCategory {

    private SearchReq category;
    private SearchReq post;

    public SearchReq getCategory() {
        return category;
    }

    public void setCategory(SearchReq category) {
        this.category = category;
    }

    public SearchReq getPost() {
        return post;
    }

    public void setPost(SearchReq post) {
        this.post = post;
    }
}
