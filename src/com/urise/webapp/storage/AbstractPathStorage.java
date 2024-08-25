package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private final Path directory;

    protected abstract void doWrite(Resume r, OutputStream os) throws IOException;

    protected abstract Resume doRead(InputStream is) throws IOException;

    protected AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    public void clear() {
        try (Stream<Path> stream = Files.list(directory)) {
            stream.forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        try (Stream<Path> stream = Files.list(directory)) {
            return (int) stream.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void doUpdate(Path path, Resume r) {
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            doWrite(r, outputStream);
        } catch (IOException e) {
            throw new StorageException("Couldn't create Path " + path.getFileName().toString(), r.getUuid(), e);
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void doSave(Path path, Resume r) {
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            doWrite(r, outputStream);
        } catch (IOException e) {
            throw new StorageException("Couldn't create Path " + path.getFileName().toString(), r.getUuid(), e);
        }
        doUpdate(path, r);
    }

    @Override
    protected Resume doGet(Path path) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return doRead(inputStream);
        } catch (IOException e) {
            throw new StorageException("Path read error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected List<Resume> getResumeList() {
        try (Stream<Path> stream = Files.list(directory)) {
            return stream.map(this::doGet).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
