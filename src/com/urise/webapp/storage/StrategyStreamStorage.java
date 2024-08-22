package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.List;

public class StrategyStreamStorage {
    private Storage storage;

    public StrategyStreamStorage(Storage storage) {
        this.storage = storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void clear() {
        storage.clear();
    }

    public void update(Resume r) {
        storage.update(r);
    }

    public void save(Resume r) {
        storage.save(r);
    }

    public Resume get(String uuid) {
        return storage.get(uuid);
    }

    public void delete(String uuid) {
        storage.delete(uuid);
    }

    public List<Resume> getAllSorted() {
        return storage.getAllSorted();
    }

    public int getSize() {
        return storage.getSize();
    }
}
