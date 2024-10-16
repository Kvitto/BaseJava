package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger log = Logger.getLogger(AbstractStorage.class.getName());

    @Override
    public void update(Resume r) {
        //log.info("update " + r);
        SK searchKey = getExistingSearchKey(r.getUuid());
        doUpdate(searchKey, r);
    }

    @Override
    public void save(Resume r) {
        //log.info("save " + r);
        SK searchKey = getNotExistingSearchKey(r.getUuid());
        doSave(searchKey, r);
    }

    @Override
    public Resume get(String uuid) {
        //log.info("get " + uuid);
        SK searchKey = getExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    @Override
    public void delete(String uuid) {
        //log.info("delete " + uuid);
        SK searchKey = getExistingSearchKey(uuid);
        doDelete(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        //log.info("getAllSorted");
        List<Resume> resumeList = getResumeList();
        Collections.sort(resumeList);
        return resumeList;
    }

    protected SK getExistingSearchKey(String uuid) {
        //log.warning("getExistingSearchKey " + uuid);
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) throw new NotExistStorageException(uuid);
        return searchKey;
    }

    protected SK getNotExistingSearchKey(String uuid) {
        //log.warning("getNotExistingSearchKey " + uuid);
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) throw new ExistStorageException(uuid);
        return searchKey;
    }

    protected abstract List<Resume> getResumeList();

    protected abstract boolean isExist(SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doSave(SK searchKey, Resume r);

    protected abstract void doUpdate(SK searchKey, Resume r);

    protected abstract SK getSearchKey(String uuid);
}
