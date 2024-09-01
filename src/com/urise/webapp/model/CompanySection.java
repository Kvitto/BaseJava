package com.urise.webapp.model;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CompanySection extends Section {
    private static final long SerialVersionUID = 1L;

    private final List<Company> companies;

    public CompanySection(List<Company> companies) {
        Objects.requireNonNull(companies, "organizations must not be null");
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
            for (Company.Position position : company.getPositions()) {
                sb.append(position.getStartDate()).append(" - ").append(position.getEndDate()).append("\n").append(position.getTitle())
                        .append("\n").append(position.getDescription().isBlank() ? "" : position.getDescription() + "\n");
            }
            if (companiesIterator.hasNext()) sb.append("\n");
        }
        return sb.toString();
    }
}
