package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPass) {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPass);
    }

    @Override
    public void clear() {
        sqlHelper.query("DELETE FROM resume", null, PreparedStatement::execute);
    }

    @Override
    public void update(Resume r) {
        if (sqlHelper.query("UPDATE resume r SET full_name=? WHERE r.uuid=?",
                new String[]{r.getFullName(), r.getUuid()}, PreparedStatement::executeUpdate) == 0) {
            throw new NotExistStorageException(r.getUuid());
        }
    }

    @Override
    public void save(Resume r) {
        sqlHelper.query("INSERT INTO resume (uuid, full_name) VALUES (?, ?)",
                new String[]{r.getUuid(), r.getFullName()}, PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        String name = sqlHelper.query("SELECT * FROM resume r WHERE r.uuid = ?",
                new String[]{uuid}, ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    return rs.getString("full_name");
                });
        return new Resume(uuid, name);
    }

    @Override
    public void delete(String uuid) {
        if (sqlHelper.query("DELETE FROM resume WHERE uuid = ?",
                new String[]{uuid}, PreparedStatement::executeUpdate) == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        sqlHelper.query("SELECT * FROM resume",null, ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
            return null;
        });
        return resumes;
    }

    @Override
    public int size() {
        return sqlHelper.query("SELECT count(*) AS count_rows FROM resume",null, ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException("db error");
            }
            return rs.getInt("count_rows");
        });
    }
}
