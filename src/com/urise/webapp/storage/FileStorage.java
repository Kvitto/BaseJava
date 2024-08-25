package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class FileStorage extends AbstractFileStorage {
    protected FileStorage(File directory, SerializerStrategy serializer) {
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
