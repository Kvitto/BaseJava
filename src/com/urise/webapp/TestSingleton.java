package com.urise.webapp;

import com.urise.webapp.model.SectionType;

public class TestSingleton {
    private static TestSingleton ourInstance;

    private TestSingleton() {
    }

    public static TestSingleton getOurInstance() {
        if (ourInstance == null) {
            ourInstance = new TestSingleton();
        }
        return ourInstance;
    }

    public static void main(String[] args) {
        for (SectionType type : SectionType.values()) {
            System.out.println(type.getTitle());
        }
    }
}
