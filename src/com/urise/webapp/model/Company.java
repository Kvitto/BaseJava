package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class Company {
    private final String website;
    private final List<Period> periods;

    public Company(String website, List<Period> periods) {
        this.website = website;
        this.periods = periods;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public String getWebsite() {
        return website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(periods, company.periods) && Objects.equals(website, company.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periods, website);
    }

    @Override
    public String toString() {
        return "Company{" +
                "website='" + website + '\'' +
                ", periods=" + periods +
                '}';
    }
}
