package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {
    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void insertToArray(Resume r, int index) {
        storage[size] = r;
        size++;
    }

    @Override
    protected void excludeFromArray(int index) {
        storage[index] = storage[--size];
        storage[size] = null;
    }
}
