package tech.caols.infinitely.rest;

public class Bean {

    private String string;
    private boolean aBoolean;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "string='" + string + '\'' +
                ", aBoolean=" + aBoolean +
                '}';
    }
}
