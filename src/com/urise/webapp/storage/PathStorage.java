package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.SerializerStrategy;

import java.io.InputStream;
import java.io.OutputStream;

public class PathStorage extends AbstractPathStorage {
    protected PathStorage(String directory, SerializerStrategy serializer) {
        super(serializer, directory);
    }

    @Override
    protected void doWrite(Resume r, OutputStream os) {
        serializer.doWrite(r, os);
    }

    @Override
    protected Resume doRead(InputStream is) {
        return serializer.doRead(is);
    }

}
