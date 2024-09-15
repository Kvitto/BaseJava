package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;
    private final SqlHelper sqlHelper;

    public SqlStorage(Properties properties) {
        this.connectionFactory = () -> DriverManager.getConnection(properties.getProperty("db.url"),
                properties.getProperty("db.user"), properties.getProperty("db.password"));
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public void clear() {
        sqlHelper.query("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void update(Resume r) {
        sqlHelper.query("UPDATE resume r SET full_name=? WHERE r.uuid=?",
                ps -> {
                    ps.setString(1, r.getFullName());
                    ps.setString(2, r.getUuid());
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(r.getUuid());
                    }
                    return null;
                });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.query("INSERT INTO resume (uuid, full_name) VALUES (?, ?)",
                ps -> {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, r.getFullName());
                    ps.execute();
                    return null;
                });
    }

    @Override
    public Resume get(String uuid) {
        String name = sqlHelper.query("SELECT * FROM resume r WHERE r.uuid = ?",
                ps -> {
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
        sqlHelper.query("DELETE FROM resume WHERE uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(uuid);
                    }
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        sqlHelper.query("SELECT * FROM resume ORDER BY full_name", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
            }
            return null;
        });
        return resumes;
    }

    @Override
    public int size() {
        return sqlHelper.query("SELECT count(*) AS count_rows FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException("db error");
            }
            return rs.getInt("count_rows");
        });
    }
}
