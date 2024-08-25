package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "Directory must be not null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    protected abstract Resume doRead(InputStream is) throws IOException;

    protected abstract void doWrite(Resume r, OutputStream os) throws IOException;

    @Override
    public void clear() {
        for (File file : dirContent()) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        return dirContent().length;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doUpdate(File file, Resume r) {
        try {
            doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", r.getUuid(), e);
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doSave(File file, Resume r) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("IO error", r.getUuid(), e);
        }
        doUpdate(file, r);
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("file not deleted", file.getName());
        }
    }

    @Override
    protected List<Resume> getResumeList() {
        List<Resume> resumeList = new ArrayList<>();
        for (File file : dirContent()) {
            resumeList.add(doGet(file));
        }
        return resumeList;
    }

    private File[] dirContent() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory must be not null");
        }
        return files;
    }
}
