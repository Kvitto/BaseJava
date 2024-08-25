package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.InputStream;
import java.io.OutputStream;

public class XmlSerializer implements SerializerStrategy {
    @Override
    public void doWrite(Resume r, OutputStream os) {

    }

    @Override
    public Resume doRead(InputStream is) {
        return null;
    }
}
