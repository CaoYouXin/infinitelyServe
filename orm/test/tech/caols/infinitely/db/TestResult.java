package tech.caols.infinitely.db;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TestResult {

    @Column(name = "ids")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
