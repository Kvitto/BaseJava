package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFile {
    public static void main(String[] args) {
        String filePath = "basejava/.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("basejava/src/com/urise/webapp");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        listFilesRecursively(0, new File("basejava/src/com/urise/webapp"));
    }

    public static void listFilesRecursively(int level, File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println(" ".repeat(level) + file.getAbsolutePath());
                listFilesRecursively((level + 1), file);
            } else {
                System.out.println(" ".repeat(level) + file.getAbsolutePath());
            }
        }
    }
}