package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage{
    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void insertToArray(Resume r, int index) {
        int insert = -index - 1;
        if (insert != size) {
            for (int i = size; i > insert; i--) {
                storage[i] = storage[i - 1];
            }
        }
        storage[insert] = r;
        size++;
    }

    @Override
    protected void excludeFromArray(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
        storage[--size] = null;
    }
}
