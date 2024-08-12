package com.urise.webapp.model;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CompanySection extends Section {
    private final List<Company> companies;

    public CompanySection(List<Company> companies) {
        this.companies = companies;
    }

    public void addCompany(Company company) {
        companies.add(company);
    }

    public List<Company> getCompanies() {
        return companies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanySection that = (CompanySection) o;
        return Objects.equals(companies, that.companies);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(companies);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Company> companiesIterator = companies.iterator();
        while (companiesIterator.hasNext()) {
            Company company = companiesIterator.next();
            sb.append(company.getWebsite()).append("\n");
            for (Period period : company.getPeriods()) {
                sb.append(period.getFrom()).append(" - ").append(period.getTo()).append("\n").append(period.getTitle())
                        .append("\n").append(period.getDescription().isBlank() ? "" : period.getDescription() + "\n");
            }
            if (companiesIterator.hasNext()) sb.append("\n");
        }
        return sb.toString();
    }
}
