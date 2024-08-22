package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.urise.webapp.ResumeTestData.createResume;
import static org.junit.Assert.assertEquals;

public class StrategyStreamStorageTest {
    protected StrategyStreamStorage strategy =
            new StrategyStreamStorage(StrategyStreamStorageFactory.getStrategy(StrategyType.PATH));

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    private static final Resume RESUME_1;
    private static final Resume RESUME_2;
    private static final Resume RESUME_3;
    private static final Resume RESUME_4;

    static {
        RESUME_1 = createResume(UUID_1, "firstResume");
        RESUME_2 = createResume(UUID_2, "secondResume");
        RESUME_3 = createResume(UUID_3,"thirdResume");
        RESUME_4 = createResume(UUID_4, "forthResume");
    }

    @Before
    public void setUp() throws Exception {
        strategy.clear();
        strategy.save(RESUME_1);
        strategy.save(RESUME_2);
        strategy.save(RESUME_3);
    }

    @Test
    public void clear() {
        strategy.clear();
        assertSize(0);
    }

    @Test
    public void update() {
        Resume newResume = createResume(UUID_1, "newFirstResume");
        strategy.update(newResume);
        assertEquals(newResume, strategy.get(UUID_1));
    }

    @Test
    public void save() throws Exception {
        strategy.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }

    @Test
    public void get() throws Exception {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        strategy.delete(UUID_1);
        assertSize(2);
        strategy.get(UUID_1);
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> list = strategy.getAllSorted();
        assertEquals(3, list.size());
        assertEquals(list, Arrays.asList(RESUME_1, RESUME_2, RESUME_3));
    }

    @Test
    public void getSize() {
        assertSize(3);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        strategy.save(RESUME_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        strategy.get("dummy");
    }

    private void assertGet(Resume r) {
        assertEquals(r, strategy.get(r.getUuid()));
    }

    private void assertSize(int size) {
        assertEquals(size, strategy.getSize());
    }
}