package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.ObjectStreamStreamSerializer;

public class PathStorageTest extends AbstractStorageTest {
    public PathStorageTest() {
        super(new PathStorage(new ObjectStreamStreamSerializer(), STORAGE_DIR.getPath()));
    }
}