package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class Company {
    private final Link homePage;
    private final List<Period> periods;

    public Company(String name, String url, List<Period> periods) {
        this.homePage = new Link(name, url);
        this.periods = periods;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public Link getWebsite() {
        return homePage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(homePage, company.homePage) && Objects.equals(periods, company.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePage, periods);
    }
}
