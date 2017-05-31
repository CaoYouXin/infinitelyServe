package tech.caols.infinitely.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreRes {

    private Map<String, String> set;
    private List<String> remove;

    public Map<String, String> getSet() {
        return set;
    }

    public void setSet(Map<String, String> set) {
        this.set = set;
    }

    public List<String> getRemove() {
        return remove;
    }

    public void setRemove(List<String> remove) {
        this.remove = remove;
    }

    public PreRes appendSet(String key, String value) {
        if (this.getSet() == null) {
            this.setSet(new HashMap<>());
        }
        this.getSet().put(key, value);
        return this;
    }

    public PreRes appendRemove(String key) {
        if (this.getRemove() == null) {
            this.setRemove(new ArrayList<>());
        }
        this.getRemove().add(key);
        return this;
    }
}
