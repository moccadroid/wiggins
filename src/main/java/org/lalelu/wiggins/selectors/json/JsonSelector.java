package org.lalelu.wiggins.selectors.json;

public class JsonSelector {
    private String selectorPath = "";
    private String field;

    public JsonSelector(String path) {
        String[] parts = path.split(":");
        char firstLetter = Character.toUpperCase(parts[0].charAt(0));
        this.field = firstLetter + parts[0].substring(1);
        this.selectorPath = parts[1];
    }

    public String getField() {
        return this.field;
    }

    public String getSelectorPath() {
        return this.selectorPath;
    }

    public String toString() {
        String toString = "";
        toString += "selectorPath: " + this.selectorPath + "\n";
        toString += "field: " + this.field + "\n";
        return toString;
    }
}
