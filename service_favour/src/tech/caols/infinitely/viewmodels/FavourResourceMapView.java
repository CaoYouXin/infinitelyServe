package tech.caols.infinitely.viewmodels;

public class FavourResourceMapView {

    private Long id;
    private int favourValue;
    private Long resourceLevelId;
    private String resourceLevelName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFavourValue() {
        return favourValue;
    }

    public void setFavourValue(int favourValue) {
        this.favourValue = favourValue;
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
