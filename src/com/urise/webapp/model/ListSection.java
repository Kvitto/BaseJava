package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class ListSection extends Section {
    private final List<String> descriptions;

    public ListSection(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public void addDescription(String string) {
        descriptions.add(string);
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return Objects.equals(descriptions, that.descriptions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(descriptions);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        descriptions.forEach(c -> sb.append(c).append("\n"));
        return sb.toString();
    }
}
