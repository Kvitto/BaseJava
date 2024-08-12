package com.urise.webapp.model;

public enum ContactType {
    PHONE("Tel."),
    SKYPE("Skype"),
    MAIL("eMail"),
    LINKEDIN("LinkedIn"),
    GITHUB("GitHub"),
    STACKOVERFLOW("Stackoverflow"),
    HOMEPAGE("Home page");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
