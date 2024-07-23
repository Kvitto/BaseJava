package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {
    protected final Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.getSize());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.getSize());
    }

    @Test
    public void update() throws Exception {
        Resume resume = new Resume("uuid1");
        storage.update(resume);
        Assert.assertEquals(resume, storage.get("uuid1"));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        Resume resume = new Resume("dummy");
        storage.update(resume);
    }

    @Test
    public void getAll() throws Exception {
        Assert.assertEquals(3, storage.getAll().length);
    }

    @Test
    public void save() throws Exception {
        Resume resume = new Resume("uuidTest");
        storage.save(resume);
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        Resume resume = new Resume("uuidTest");
        storage.save(resume);
        storage.save(resume);
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() throws Exception {
        int length = AbstractArrayStorage.STORAGE_LIMIT - storage.getSize();
        for (int i = 0; i < length; i++) {
            try {
                storage.save(new Resume());
            } catch (StorageException e) {
                System.out.println("Ошибка: переполнение произошло раньше времени.");
            }
        }
        storage.save(new Resume());
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_1);
        Assert.assertEquals(2, storage.getSize());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete("dummy");
    }

    @Test
    public void get() throws Exception {
        Resume resume = new Resume("uuidTest");
        storage.save(resume);
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }
}