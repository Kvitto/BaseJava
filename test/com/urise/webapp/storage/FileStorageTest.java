package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.ObjectStreamStreamSerializer;

public class FileStorageTest extends AbstractStorageTest {
    public FileStorageTest() {
        super(new FileStorage(new ObjectStreamStreamSerializer(), STORAGE_DIR));
    }

}