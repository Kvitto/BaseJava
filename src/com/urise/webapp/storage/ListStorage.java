package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage{
    protected ArrayList<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int getSize() {
        return storage.size();
    }

    @Override
    protected void doDelete(int index) {
        storage.remove(index);
    }

    @Override
    protected Resume doGet(int index) {
        return storage.get(index);
    }

    @Override
    protected void doSave(Resume r, int index) {
        storage.add(r);
    }

    @Override
    protected void doUpdate(int index, Resume r) {
        storage.add(index, r);
    }

    @Override
    protected void fillDeletedElement(int index) {}

    @Override
    protected void insertElement(Resume r, int index) {}

    @Override
    protected int getIndex(String uuid) {
        for (Resume r : storage) {
            if (uuid.equals(r.getUuid())) {
                return storage.indexOf(r);
            }
        }
        return -1;
    }
}
