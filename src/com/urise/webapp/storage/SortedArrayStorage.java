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
            int tempSize = size - insert;
            Resume[] tempStorage = new Resume[tempSize];
            System.arraycopy(storage, insert, tempStorage, 0, tempSize);
            System.arraycopy(tempStorage, 0, storage, insert + 1, tempSize);
        }
        storage[insert] = r;
    }

    @Override
    protected void excludeFromArray(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
    }
}
