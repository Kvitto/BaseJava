package com.urise.webapp.storage;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapStorageTest extends AbstractArrayStorageTest {
    public MapStorageTest() {
        super(new MapStorage());
    }

    @Ignore
    @Test
    @Override
    public void saveOverflow() throws Exception {};

    @Override
    @Test
    public void getAll() throws Exception {
        Assert.assertEquals(3, storage.getAll().length);
    }
}