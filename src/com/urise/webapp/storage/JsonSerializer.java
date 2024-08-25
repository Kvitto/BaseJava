package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.InputStream;
import java.io.OutputStream;

public class JsonSerializer implements SerializerStrategy {
    @Override
    public void doWrite(Resume r, OutputStream os) {

    }

    @Override
    public Resume doRead(InputStream is) {
        return null;
    }
}
