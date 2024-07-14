package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];
    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        String uuid = resume.getUuid();
        int index = findIndex(uuid);
        if (index >= 0) {
            storage[index] = resume;
            System.out.println("Резюме c UUID(" + uuid + ") успешно обновлено!");
        }
    }

    public void save(Resume resume) {
        if (size >= storage.length) {
            System.out.println("Ошибка: хранилище переполнено!");
            return;
        }
        String uuid = resume.getUuid();
        if (findIndex(uuid) >= 0) {
            System.out.println("Резюме c UUID(" + uuid + ") уже есть в базе!");
            return;
        }
        storage[size++] = resume;
        System.out.println("Резюме c UUID(" + uuid + ") успешно добавлено в базу!");
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) return storage[index];
        return null;
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) return;
        storage[index] = storage[--size];
        storage[size] = null;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int getSize() {
        return size;
    }

    private int findIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        System.out.println("Резюме c UUID(" + uuid + ") НЕ найдено!");
        return -1;
    }
}
