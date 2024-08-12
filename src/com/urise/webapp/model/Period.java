package com.urise.webapp.model;

import java.time.LocalDate;

public class Period {
    private LocalDate from;
    private LocalDate to;
    private String title;
    private String description;

    public Period(LocalDate from, LocalDate to, String title, String description) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "Period{" +
                "from=" + from +
                ", to=" + to +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
