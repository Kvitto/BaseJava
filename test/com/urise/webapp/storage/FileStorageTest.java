package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.StreamSerializer;

public class FileStorageTest extends AbstractStorageTest {
    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new StreamSerializer()));
    }

}