package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.DataStreamSerializer;

public class DataPathStorageTest extends AbstractStorageTest {
    public DataPathStorageTest() {
        super(new PathStorage(new DataStreamSerializer(), STORAGE_DIR.getPath()));
    }
}