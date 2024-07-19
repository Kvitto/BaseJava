package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage{
    @Override
    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        int insert = -index - 1;
        if (index > -1) {
            System.out.println("Resume " + r.getUuid() + " already exist");
        } else if (size >= STORAGE_LIMIT) {
            System.out.println("Storage overflow");
        } else if (insert == size) {
            storage[size++] = r;
        } else {
            insertToArray(r, insert);
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("Resume " + uuid + " not exist");
        } else {
            System.arraycopy(storage, index + 1, storage, index, size - index - 1);
            storage[--size] = null;
        }
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    private void insertToArray(Resume r, int index) {
        for (int i = size; i > index; ) {
            storage[i] = storage[--i];
        }
        storage[index] = r;
        size++;
    }
}
