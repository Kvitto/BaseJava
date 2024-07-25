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
    private static final Resume RESUME_1 = new Resume("uuid1");
    private static final Resume RESUME_2 = new Resume("uuid2");
    private static final Resume RESUME_3 = new Resume("uuid3");
    private static final Resume RESUME_4 = new Resume("uuid4");

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() throws Exception {
        Assert.assertTrue(assertSize(3));
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertTrue(assertSize(0));
        Resume[] expected = new Resume[storage.getSize()];
        Assert.assertArrayEquals(expected, storage.getAll());
    }

    @Test
    public void update() throws Exception {
        Resume resume = new Resume(RESUME_2.getUuid());
        storage.update(resume);
        Assert.assertSame(resume, storage.get(RESUME_2.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(RESUME_4);
    }

    @Test
    public void getAll() throws Exception {
        Resume[] expected = {RESUME_1, RESUME_2, RESUME_3};
        Assert.assertArrayEquals(expected, storage.getAll());
    }

    @Test
    public void save() throws Exception {
        int size = storage.getSize();
        storage.save(RESUME_4);
        Assert.assertTrue(assertGet(RESUME_4));
        Assert.assertTrue(assertSize(size + 1));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(RESUME_1);
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() throws Exception {
        storage.clear();
        int length = AbstractArrayStorage.STORAGE_LIMIT;
        for (int i = 0; i < length; i++) {
            try {
                storage.save(new Resume());
            } catch (StorageException e) {
                Assert.fail("Ошибка: переполнение произошло раньше времени.");
            }
        }
        storage.save(new Resume());
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        int size = storage.getSize();
        storage.delete(RESUME_1.getUuid());
        Assert.assertTrue(assertSize(size - 1));
        Assert.assertTrue(assertGet(RESUME_1));
    }

    @Test
    public void get() throws Exception {
        Assert.assertTrue(assertGet(RESUME_1));
        Assert.assertTrue(assertGet(RESUME_2));
        Assert.assertTrue(assertGet(RESUME_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }

    private boolean assertSize(int size) {
        return size == storage.getSize();
    }

    private boolean assertGet(Resume resume) {
        return resume.equals(storage.get(resume.getUuid()));
    }
}