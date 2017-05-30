package tech.caols.infinitely.datamodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "favour_resource_map")
public class FavourResourceMapData {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "favour_value")
    private int favourValue;

    @Column(name = "resource_level_id")
    private Long resourceLevelId;

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
}
