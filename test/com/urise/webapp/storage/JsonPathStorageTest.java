package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.JsonStreamSerializer;

public class JsonPathStorageTest extends AbstractStorageTest {
    public JsonPathStorageTest() {
        super(new PathStorage(new JsonStreamSerializer(), STORAGE_DIR.getPath()));
    }
}